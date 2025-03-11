package com.handlers;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.logging.Level;
import com.dao.*;
import com.dbObjects.*;
import com.dbconn.Database;
import com.enums.*;
import com.filters.SessionFilter;
import com.google.gson.*;
import com.loggers.AppLogger;
import com.queryLayer.*;

public class GoogleContactsSyncHandler {
    private static final AppLogger logger = new AppLogger(GoogleContactsSyncHandler.class.getCanonicalName());
    
    // Configuration constants
    private static final String CLIENT_ID = Database.AppProp.getProperty("google_client_id");
    private static final String CLIENT_SECRET = Database.AppProp.getProperty("google_client_secret");
    private static final String PROFILE_SCOPE = Database.AppProp.getProperty("google_profile_scope") + " "+
                                              Database.AppProp.getProperty("google_email_scope");
    private static final String CONTACT_SCOPE = Database.AppProp.getProperty("google_contacts_scope");
    private static final String REDIRECT_URI = Database.AppProp.getProperty("google_contacts_sync_redirect_uri");
    private static final String PEOPLE_CONTACTS_ENDPOINT = Database.AppProp.getProperty("google_people_contacts_endpoint");
    private static final String PEOPLE_ACCOUNT_ENDPOINT = Database.AppProp.getProperty("google_people_profile_endpoint");
    private static final String TOKEN_ENDPOINT = Database.AppProp.getProperty("google_token_endpoint");
    private static final String ACCOUNT_ENDPOINT = Database.AppProp.getProperty("google_account_endpoint");
    private static final String AUTH_URL_TEMPLATE = 
        "https://accounts.google.com/o/oauth2/v2/auth" +
        "?client_id=%s" +
        "&redirect_uri=%s" +
        "&response_type=code" +
        "&scope=%s %s" +
        "&access_type=offline" +
        "&state=%s" +
        "&prompt=consent";

    private final String sessionId;
    private final ContactsDao contactsDao;
    private final ContactMobileNumbersDao mobileNumbersDao;
    private final ContactMailsDao mailsDao;

    public GoogleContactsSyncHandler() {
        this.sessionId = SessionFilter.SESSION_ID.get();
        this.contactsDao = new ContactsDao();
        this.mobileNumbersDao = new ContactMobileNumbersDao();
        this.mailsDao = new ContactMailsDao();
    }
    
    
    public String getAuthUrl() {
        return String.format(AUTH_URL_TEMPLATE,
            CLIENT_ID, REDIRECT_URI, PROFILE_SCOPE, CONTACT_SCOPE, sessionId);
    }

    public String getAccountId(String accessToken) {
        try {
            JsonObject response = executeGetRequest(PEOPLE_ACCOUNT_ENDPOINT, accessToken);
            return response != null ? response.get("resourceName").getAsString() : null;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error fetching account ID", e);
            return null;
        }
    }

    public String getAccessToken(String refreshToken) {
        try {
            StringBuilder params = new StringBuilder();
            params.append("client_id=").append(CLIENT_ID)
                  .append("&client_secret=").append(CLIENT_SECRET)
                  .append("&refresh_token=").append(refreshToken)
                  .append("&grant_type=refresh_token");

            JsonObject response = executePostRequest(TOKEN_ENDPOINT, params.toString());
            return response != null ? response.get("access_token").getAsString() : null;
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Error getting access token", e);
            return null;
        }
    }

    public boolean getAndHandleContacts(ContactsSyncObj syncObj, String accessToken, String pageToken) {
        try {
            String endpoint = pageToken == null ? PEOPLE_CONTACTS_ENDPOINT : 
                            PEOPLE_CONTACTS_ENDPOINT + "&pageToken=" + pageToken;
            
            JsonObject response = executeGetRequest(endpoint, accessToken);
            if (response == null) {
                return false;
            }

            Map<String, Long> existingContacts = contactsDao.getExistingContacts(
                syncObj.getUserId(), syncObj.getRefreshToken());
            
            if (response.has("connections")) {
                processContacts(response.getAsJsonArray("connections"), 
                              existingContacts, syncObj);
            }

            if (!existingContacts.isEmpty()) {
                deleteContacts(existingContacts);
            }

            if (response.has("nextPageToken")) {
                return getAndHandleContacts(syncObj, accessToken, 
                                         response.get("nextPageToken").getAsString());
            }
            
            return true;
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error processing contacts", e);
            return false;
        }
    }

    private void processContacts(JsonArray contacts, Map<String, Long> existingContacts, 
                               ContactsSyncObj syncObj) throws DaoException {
        for (JsonElement element : contacts) {
            JsonObject contact = element.getAsJsonObject();
            String resourceName = contact.get("resourceName").getAsString();
            long modifiedTime = getModifiedTime(contact);

            if (existingContacts.containsKey(resourceName)) {
                if (modifiedTime > existingContacts.get(resourceName)) {
                    updateContact(contact);
                }
                existingContacts.remove(resourceName);
            } else {
                createContact(contact, syncObj.getRefreshToken(), syncObj.getUserId());
            }
        }
    }

    private long getModifiedTime(JsonObject contact) {
        JsonObject metadata = contact.getAsJsonObject("metadata");
        JsonArray sources = metadata.getAsJsonArray("sources");
        String timeString = sources.get(0).getAsJsonObject().get("updateTime").getAsString();
        return OffsetDateTime.parse(timeString).toInstant().toEpochMilli();
    }

    private void createContact(JsonObject contact, String refreshToken, int userId) 
            throws DaoException {
        String resourceName = contact.get("resourceName").getAsString();
        String[] names = extractNames(contact);
        long modifiedTime = getModifiedTime(contact);

        int contactId = contactsDao.syncAddContact(
            names[0], names[1], userId, resourceName, refreshToken, modifiedTime);

        processPhoneNumbers(contactId, contact);
        processEmails(contactId, contact);
    }

    private String[] extractNames(JsonObject contact) {
        String firstName = "No name";
        String lastName = "No name";

        if (contact.has("names")) {
            JsonArray names = contact.getAsJsonArray("names");
            if (names.size() > 0) {
                JsonObject name = names.get(0).getAsJsonObject();
                if (name.has("displayName")) {
                    String[] nameParts = name.get("displayName").getAsString().split(" ");
                    firstName = nameParts[0];
                    lastName = nameParts.length > 1 ? nameParts[1] : firstName;
                }
            }
        }

        return new String[]{firstName, lastName};
    }

    private void processPhoneNumbers(int contactId, JsonObject contact) throws DaoException {
        if (!contact.has("phoneNumbers")) {
            return;
        }

        JsonArray numbers = contact.getAsJsonArray("phoneNumbers");
        for (JsonElement number : numbers) {
            String phoneNumber = number.getAsJsonObject().get("value").getAsString();
            mobileNumbersDao.addNumberToContactId(contactId, phoneNumber);
        }
    }

    private void processEmails(int contactId, JsonObject contact) throws DaoException {
        if (!contact.has("emailAddresses")) {
            return;
        }

        JsonArray emails = contact.getAsJsonArray("emailAddresses");
        for (JsonElement email : emails) {
            String emailAddress = email.getAsJsonObject().get("value").getAsString();
            mailsDao.addMailToContact(contactId, emailAddress);
        }
    }

    private void updateContact(JsonObject contact) throws DaoException {
        String refId = contact.get("resourceName").getAsString();
        ContactsObj existingContact = contactsDao.getContactWithRefId(refId);
        if (existingContact == null) {
            return;
        }

        String[] names = extractNames(contact);
        contactsDao.updateContact(existingContact.getContactId(), names[0], names[1]);

        updatePhoneNumbers(existingContact.getContactId(), contact);
        updateEmails(existingContact.getContactId(), contact);
    }

    private void updatePhoneNumbers(int contactId, JsonObject contact) throws DaoException {
        List<ContactMobileNumbersObj> existingNumbers = 
            mobileNumbersDao.getContactMobileNumbers(contactId);
        Set<String> currentNumbers = new HashSet<>();
        
        for (ContactMobileNumbersObj number : existingNumbers) {
            currentNumbers.add(number.getMobileNumber());
        }

        if (contact.has("phoneNumbers")) {
            JsonArray newNumbers = contact.getAsJsonArray("phoneNumbers");
            Set<String> updatedNumbers = new HashSet<>();

            for (JsonElement number : newNumbers) {
                String phoneNumber = number.getAsJsonObject().get("value").getAsString();
                updatedNumbers.add(phoneNumber);
                
                if (!currentNumbers.contains(phoneNumber)) {
                    mobileNumbersDao.addNumberToContactId(contactId, phoneNumber);
                }
            }

            // Delete numbers that no longer exist
            currentNumbers.removeAll(updatedNumbers);
            for (String number : currentNumbers) {
                mobileNumbersDao.deleteNumberWithContactId(contactId, number);
            }
        }
    }

    private void updateEmails(int contactId, JsonObject contact) throws DaoException {
        List<ContactMailsObj> existingMails = mailsDao.getMailsWithContactId(contactId);
        Set<String> currentEmails = new HashSet<>();
        
        for (ContactMailsObj mail : existingMails) {
            currentEmails.add(mail.getEmail());
        }

        if (contact.has("emailAddresses")) {
            JsonArray newEmails = contact.getAsJsonArray("emailAddresses");
            Set<String> updatedEmails = new HashSet<>();

            for (JsonElement email : newEmails) {
                String emailAddress = email.getAsJsonObject().get("value").getAsString();
                updatedEmails.add(emailAddress);
                
                if (!currentEmails.contains(emailAddress)) {
                    mailsDao.addMailToContact(contactId, emailAddress);
                }
            }

            // Delete emails that no longer exist
            currentEmails.removeAll(updatedEmails);
            for (String email : currentEmails) {
                mailsDao.deleteMailForContact(contactId, email);
            }
        }
    }

    private void deleteContacts(Map<String, Long> deletedContacts) throws QueryException {
        Delete delete = new Delete();
        for (Map.Entry<String, Long> entry : deletedContacts.entrySet()) {
            delete.table(Table.Contacts)
                  .condition(Contacts.REF_ID, Operators.Equals, entry.getKey())
                  .executeUpdate();
        }
    }

    private JsonObject executeGetRequest(String endpoint, String accessToken) throws IOException {
        HttpURLConnection conn = createConnection(endpoint, "GET");
        conn.setRequestProperty("Authorization", "Bearer " + accessToken);
        conn.setRequestProperty("Accept", "application/json");

        return handleResponse(conn);
    }

    private JsonObject executePostRequest(String endpoint, String params) throws IOException {
        HttpURLConnection conn = createConnection(endpoint, "POST");
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setDoOutput(true);

        try (OutputStream os = conn.getOutputStream()) {
            os.write(params.getBytes(StandardCharsets.UTF_8));
            os.flush();
        }

        return handleResponse(conn);
    }

    private HttpURLConnection createConnection(String endpoint, String method) throws IOException {
        URL url = new URL(endpoint);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(method);
        return conn;
    }

    private JsonObject handleResponse(HttpURLConnection conn) throws IOException {
        int responseCode = conn.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                System.out.println(JsonParser.parseString(response.toString()).getAsJsonObject());
                return JsonParser.parseString(response.toString()).getAsJsonObject();
            }
        }
        return null;
    }


	public static String getAccountsEndpoint() {
		return ACCOUNT_ENDPOINT;
	}


	public static String getClientid() {
		return CLIENT_ID;
	}


	public static String getProfileScope() {
		return PROFILE_SCOPE;
	}


	public static String getClientsecret() {
		return CLIENT_SECRET;
	}


	public static String getRedirecturi() {
		return REDIRECT_URI;
	}


	public static String getPeopleAccountEndpoint() {
		return ACCOUNT_ENDPOINT;
	}
}