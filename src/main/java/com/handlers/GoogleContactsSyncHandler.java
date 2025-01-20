package com.handlers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAccessor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import com.dao.ContactMailsDao;
import com.dao.ContactMobileNumbersDao;
import com.dao.ContactsDao;
import com.dao.DaoException;
import com.dao.UserDetailsDao;
import com.dbObjects.ContactMailsObj;
import com.dbObjects.ContactMobileNumbersObj;
import com.dbObjects.ContactsObj;
import com.dbObjects.UserDetailsObj;
import com.enums.Contacts;
import com.enums.Operators;
import com.enums.Table;
import com.filters.SessionFilter;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loggers.AppLogger;
import com.queryLayer.Delete;
import com.queryLayer.QueryException;
import com.session.Session;

public class GoogleContactsSyncHandler {
	private static AppLogger logger = new AppLogger(GoogleContactsSyncHandler.class.getCanonicalName());
	private static final String clientId = "278443584424-q0nbt03i92268ici8ktj5uhi3ee65044.apps.googleusercontent.com";
	private static final String clientSecret = "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";
	private static final String profileScope  = "https://www.googleapis.com/auth/userinfo.profile"
			+ " https://www.googleapis.com/auth/userinfo.email";
	private static final String contactScope = " https://www.googleapis.com/auth/contacts";
	private static final String redirectUri = "http://localhost:8280/contacts/goauth";
	private static String peopleContactsEndpoint = "https://people.googleapis.com/v1/people/me/connections?personFields=names,phoneNumbers,emailAddresses,photos,metadata";
	private static final String peopleAccountEndpoint = "https://people.googleapis.com/v1/people/me";
	private static final String accountEndpoint = "https://accounts.google.com/o/oauth2/v2/auth";
	public static String getClientid() {
		return clientId;
	}

	public static String getClientsecret() {
		return clientSecret;
	}

	public static String getProfileScope() {
		return profileScope;
	}
	
	public static String getContactsScope () {
		return contactScope;
	}

	public static String getRedirecturi() {
		return redirectUri;
	}

	public static String getState() {
		return state;
	}
	
	public static String getAccountsEndpoint() {
		return accountEndpoint;
	}
	
	public static String getPeopleAccountEndpoint() {
		return peopleAccountEndpoint;
	}

	private static final String state = Session.getSessionId();

	public String getAuthUrl() {

		// auth endpoint of google -> https://accounts.google.com/o/oauth2/auth
		String authUrl = "https://accounts.google.com/o/oauth2/v2/auth" + "?client_id=" + clientId + "&redirect_uri="
				+ redirectUri + "&response_type=code" + "&scope=" + profileScope+" "+contactScope + "&access_type=offline" + "&state=" + state
				+ "&prompt=consent";

		return authUrl;
	}
	
	
	public boolean checkAccountExists(String refreshToken, String accessToken) {
//		{
//			  "phoneNumbers": [
//			    {
//			      "canonicalForm": "+917032555311", 
//			      "formattedType": "Mobile", 
//			      "type": "mobile", 
//			      "value": "+917032555311", 
//			      "metadata": {
//			        "source": {
//			          "type": "CONTACT", 
//			          "id": "5bc2a4cb08416dfc"
//			        }, 
//			        "primary": true, 
//			        "sourcePrimary": true
//			      }
//			    }
//			  ], 
//			  "memberships": [
//			    {
//			      "contactGroupMembership": {
//			        "contactGroupId": "myContacts", 
//			        "contactGroupResourceName": "contactGroups/myContacts"
//			      }, 
//			      "metadata": {
//			        "source": {
//			          "type": "CONTACT", 
//			          "id": "5bc2a4cb08416dfc"
//			        }
//			      }
//			    }
//			  ], 
//			  "photos": [
//			    {
//			      "url": "https://lh3.googleusercontent.com/a/ACg8ocJmhwSCdhs26Yo7WJBNApuaEMK6JVtaEJy3f_ppSHsRNdsMEZm7bw=s100", 
//			      "metadata": {
//			        "source": {
//			          "type": "PROFILE", 
//			          "id": "100549746507535747515"
//			        }, 
//			        "primary": true
//			      }
//			    }, 
//			    {
//			      "url": "https://lh3.googleusercontent.com/contacts/AG6tpzHyuSYx9Sa3dMapq5ajr18oxwse_AEKMwY9OHFY-VMe6aKpmQmF=s100", 
//			      "metadata": {
//			        "source": {
//			          "type": "CONTACT", 
//			          "id": "5bc2a4cb08416dfc"
//			        }
//			      }
//			    }
//			  ], 
//			  "etag": "%EiEBAgMEBQYHCAkKCwwNDg8QERITFBUXGSIlLjQ1Nz0+P0AaBAECBQciDE9TYldFbU80Q3FRPQ==", 
//			  "names": [
//			    {
//			      "displayNameLastFirst": "_26, Karthikeya", 
//			      "displayName": "Karthikeya _26", 
//			      "familyName": "_26", 
//			      "unstructuredName": "Karthikeya _26", 
//			      "givenName": "Karthikeya", 
//			      "metadata": {
//			        "source": {
//			          "type": "PROFILE", 
//			          "id": "100549746507535747515"
//			        }, 
//			        "primary": true, 
//			        "sourcePrimary": true
//			      }
//			    }, 
//			    {
//			      "unstructuredName": "Karthikeya", 
//			      "givenName": "Karthikeya", 
//			      "displayName": "Karthikeya", 
//			      "displayNameLastFirst": "Karthikeya", 
//			      "metadata": {
//			        "source": {
//			          "type": "CONTACT", 
//			          "id": "5bc2a4cb08416dfc"
//			        }, 
//			        "sourcePrimary": true
//			      }
//			    }
//			  ], 
//			  "genders": [
//			    {
//			      "formattedValue": "Male", 
//			      "value": "male", 
//			      "metadata": {
//			        "source": {
//			          "type": "CONTACT", 
//			          "id": "5bc2a4cb08416dfc"
//			        }, 
//			        "primary": true
//			      }
//			    }
//			  ], 
//			  "resourceName": "people/100549746507535747515", 
//			  "emailAddresses": [
//			    {
//			      "value": "karthikeya.00004@gmail.com", 
//			      "metadata": {
//			        "source": {
//			          "type": "ACCOUNT", 
//			          "id": "100549746507535747515"
//			        }, 
//			        "verified": true, 
//			        "primary": true, 
//			        "sourcePrimary": true
//			      }
//			    }
//			  ], 
//			  "coverPhotos": [
//			    {
//			      "url": "https://lh3.googleusercontent.com/c5dqxl-2uHZ82ah9p7yxrVF1ZssrJNSV_15Nu0TUZwzCWqmtoLxCUJgEzLGtxsrJ6-v6R6rKU_-FYm881TTiMCJ_=s1600", 
//			      "default": true, 
//			      "metadata": {
//			        "source": {
//			          "type": "PROFILE", 
//			          "id": "100549746507535747515"
//			        }, 
//			        "primary": true
//			      }
//			    }
//			  ], 
//			  "metadata": {
//			    "sources": [
//			      {
//			        "updateTime": "2024-12-11T13:05:54.535295Z", 
//			        "etag": "#+HdgwnYWm4o=", 
//			        "type": "PROFILE", 
//			        "id": "100549746507535747515", 
//			        "profileMetadata": {
//			          "userTypes": [
//			            "GOOGLE_USER"
//			          ], 
//			          "objectType": "PERSON"
//			        }
//			      }, 
//			      {
//			        "updateTime": "2024-11-28T04:01:01.889332Z", 
//			        "etag": "#OSbWEmO4CqQ=", 
//			        "type": "CONTACT", 
//			        "id": "5bc2a4cb08416dfc"
//			      }
//			    ], 
//			    "objectType": "PERSON"
//			  }
//			}
		
		HttpURLConnection conn;
		
		try {
			URL url = new URL(peopleAccountEndpoint);
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", "Bearer " + accessToken);
			conn.setRequestProperty("Accept", "application/json");
			
			JsonObject jsonResponse = new JsonObject();
			int responseCode = conn.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(
						new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
				StringBuffer res = new StringBuffer();
				String line;
				while ((line = in.readLine()) != null) {
					res.append(line);
				}

				jsonResponse = JsonParser.parseString(res.toString()).getAsJsonObject();
				
				String accountId  = jsonResponse.get("resourceName").getAsString();
				
				UserDetailsDao dao = new UserDetailsDao();
				UserDetailsObj user = dao.getUserWithAccountId(accountId);
				if(user == null) {
					return false;
				}else {
					return true;
				}
				
				
			}else {
				 jsonResponse.addProperty("error", "Something went wrong");
				 return false;
				
			}
			
			
		}catch (Exception e) {
			
			return false;
		}
	}

	public boolean getAndCreateContacts(String refreshToken, String accesstoken, String nextPageToken) {
		// {
		// "connections" : [{},{}],
		// "nextPageToken" : "xyz",
		// "totalItems" : number,
		// "totalPeople" :
		// }

//		{
//		      "resourceName": "people/c4946087733441608968", 
//		      "etag": "%EggBAgMGLjU3PRoEAQIFByIMU0xEMmM0Wk8zNkU9", 
//		      "names": [
//		        {
//		          "honorificPrefix": "Veltech ", 
//		          "displayNameLastFirst": "Veltech Subbareddy Sir", 
//		          "displayName": "Veltech Subbareddy Sir", 
//		          "middleName": "Sir", 
//		          "unstructuredName": "Veltech Subbareddy Sir", 
//		          "givenName": "Subbareddy", 
//		          "metadata": {
//		            "source": {
//		              "type": "CONTACT", 
//		              "id": "44a408968bb43d08"
//		            }, 
//		            "primary": true, 
//		            "sourcePrimary": true
//		          }
//		        }
//		      ], 
//		      "photos": [
//		        {
//		          "url": "https://lh3.googleusercontent.com/cm/AGPWSu-6M8HjC0-mfW_erRBp6W5au_9nQhtiEH6Dl95zumjA5_NQ8CRUNOHyFMp9YucEwMiBDw=s100", 
//		          "default": true, 
//		          "metadata": {
//		            "source": {
//		              "type": "CONTACT", 
//		              "id": "44a408968bb43d08"
//		            }, 
//		            "primary": true
//		          }
//		        }
//		      ], 
//		      "metadata": {
//		        "sources": [
//		          {
//		            "updateTime": "2021-09-15T03:14:59.398784Z", 
//		            "etag": "#SLD2c4ZO36E=", 
//		            "type": "CONTACT", 
//		            "id": "44a408968bb43d08"
//		          }
//		        ], 
//		        "objectType": "PERSON"
//		      }
//		    }

		HttpURLConnection conn;

		try {
			URL url = new URL(peopleContactsEndpoint);
			if (nextPageToken != null) {
				url = new URL(peopleContactsEndpoint + "&pageToken=" + nextPageToken);
			}
			conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", "Bearer " + accesstoken);
			conn.setRequestProperty("Accept", "application/json");

			JsonObject jsonResponse = new JsonObject();
			int responseCode = conn.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(
						new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
				StringBuffer res = new StringBuffer();
				String line;
				while ((line = in.readLine()) != null) {
					res.append(line);
				}

				jsonResponse = JsonParser.parseString(res.toString()).getAsJsonObject();
				System.out.println(jsonResponse.toString());
				// get contacts ResourceNames as HashSet for fast checking
				ContactsDao cdao = new ContactsDao();

				// checkup map contains contacts
				HashMap<String, Long> checkupMap = cdao.getExistingContacts(SessionFilter.user_id.get(), refreshToken);
				JsonArray contacts = jsonResponse.get("connections").getAsJsonArray();

				for (JsonElement element : contacts) {

					JsonObject contact = (JsonObject) element;
					String resourceName = contact.get("resourceName").getAsString();
					JsonObject metaData = (JsonObject) contact.get("metadata");

					JsonArray sources = metaData.get("sources").getAsJsonArray();

					String modifiedTimeString = ((JsonObject) sources.get(0)).get("updateTime").getAsString();

					OffsetDateTime modifiedTime = OffsetDateTime.parse(modifiedTimeString);

					if (checkupMap.containsKey(resourceName)) {
						Instant i = Instant.ofEpochMilli(checkupMap.get(resourceName));
						if (modifiedTime.isAfter(i.atOffset(ZoneOffset.UTC))) {
							updateContact(contact);
						}
					} else {
						createContact(contact);
					}
				}

				// after creating and updating still if the map has entries these should be
				// deleted
				if (!checkupMap.isEmpty()) {
					deleteContact(checkupMap);
				}

			}

			if (jsonResponse.get("nextPageToken") != null) {
				System.out.println(jsonResponse.get("nextPageToken").getAsString());
				getAndCreateContacts(refreshToken, accesstoken, jsonResponse.get("nextPageToken").getAsString());
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.log(Level.WARNING, e.getMessage(),e);
			return false;
		}

		return false;

	}

	private void createContact(JsonObject contact) {
		String resourceName = contact.get("resourceName").getAsString();
//		String etag = contact.get("etag").getAsString();
		JsonArray names = null;
		if (contact.get("names") != null) {
			names = contact.get("names").getAsJsonArray();
		}
		JsonArray phoneNumbers = null;
		if (contact.get("phoneNumbers") != null) {
			phoneNumbers = contact.get("phoneNumbers").getAsJsonArray();
		}
		JsonArray emailAddresses = null;
		if (contact.get("emailAddresses") != null) {
			emailAddresses = contact.get("emailAddresses").getAsJsonArray();
		}
		JsonArray photos = null;
		if (contact.get("photos") != null) {
			photos = contact.get("photos").getAsJsonArray();
		}
		System.out.println(resourceName);
		System.out.println(names);

		JsonObject name = (names != null && names.size() > 0 && names.get(0).isJsonObject())
				? names.get(0).getAsJsonObject()
				: new JsonObject();

		String displayName = name.get("displayName") != null ? name.get("displayName").getAsString() : "No name";

		// Split displayName by spaces into an array
		String[] nameParts = displayName.split(" ");

		// Get the first name as the first part
		String firstName = nameParts[0];

		// Set last name as the second part if present, otherwise use firstName as
		// lastName
		String lastName = nameParts.length > 2 ? nameParts[2] : firstName;

		JsonObject metaData = (JsonObject) contact.get("metadata");

		JsonArray sources = metaData.get("sources").getAsJsonArray();

		String modifiedTimeString = ((JsonObject) sources.get(0)).get("updateTime").getAsString();

		OffsetDateTime modifiedTime = OffsetDateTime.parse(modifiedTimeString);
		ContactsDao contactDao = new ContactsDao();
		int contactId = contactDao.syncAddContact(firstName, lastName, SessionFilter.user_id.get(), resourceName,
				modifiedTime.toInstant().toEpochMilli());

		if (phoneNumbers != null && phoneNumbers.size() > 0) {
			ContactMobileNumbersDao mobileDao = new ContactMobileNumbersDao();
			for (JsonElement element : phoneNumbers) {
				JsonObject number = (JsonObject) element;

				try {
					mobileDao.addNumberToContactId(contactId, number.get("value").getAsString());
				} catch (DaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		if (emailAddresses != null && emailAddresses.size() > 0) {
			ContactMailsDao mailDao = new ContactMailsDao();
			for (JsonElement element : emailAddresses) {
				JsonObject mail = (JsonObject) element;
				try {
					mailDao.addMailToContact(contactId, mail.get("value").getAsString());
				} catch (DaoException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//				addEmail(contactId, mail.get("value").getAsString());
			}
		}

		// photos to be processed later
	}

	private void updateContact(JsonObject contact) {
		// get contact

		String refId = contact.get("resourceName").getAsString();
		
		JsonArray names = null;
		if(contact.get("names") != null) {
			names = contact.get("names").getAsJsonArray();
		}
		
		ContactsDao cdao = new ContactsDao();
		ContactsObj refContact = null;
		try {
			refContact = cdao.getContactWithRefId(refId);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String refFirstName = refContact.getFirstName();
		String refLastName = refContact.getLastName();
		
		ContactMobileNumbersDao ndao = new ContactMobileNumbersDao();
		List<ContactMobileNumbersObj> numbers = null;
		try {
			numbers = ndao.getContactMobileNumbers(refContact.getContactId());
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		
		ContactMailsDao mdao = new ContactMailsDao();
		List<ContactMailsObj> mails = null;
		try {
			mails = mdao.getMailsWithContactId(refContact.getContactId());
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JsonArray phoneNumbers = null;
		if (contact.get("phoneNumbers") != null) {
			phoneNumbers = contact.get("phoneNumbers").getAsJsonArray();
		}
		JsonArray emailAddresses = null;
		if (contact.get("emailAddresses") != null) {
			emailAddresses = contact.get("emailAddresses").getAsJsonArray();
		}
		
		//contact
		JsonObject name = (names != null && names.size() >0 && names.get(0).isJsonObject())
				? names.get(0).getAsJsonObject(): new JsonObject();
		
		String displayName = name.get("displayName") != null ? name.get("displayName").getAsString() : "No name";
		
		String[] nameParts = displayName.split(" ");
		
		String firstName = nameParts[0];
		String lastName = nameParts.length >= 2 ? nameParts[1] : firstName;
		try {
			cdao.updateContact(refContact.getContactId(),firstName, lastName);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//phone numbers
		if (phoneNumbers != null && phoneNumbers.size() > 0) {
			handleNumbers(numbers, emailAddresses);
		}
		//email addresses
		if (emailAddresses != null && emailAddresses.size() > 0) {
			handleEmails(mails, emailAddresses);
		}

		return;

	}
	
	private void handleNumbers(List<ContactMobileNumbersObj> refNumbers, JsonArray numbersRecievedFromGoogle) {
		// convert list to a hashmap to optimize time
		// tc now : n square , n if hashmap used
//		"phoneNumbers": [
//		                 {
//		                   "canonicalForm": "+917032555311", 
//		                   "formattedType": "Mobile", 
//		                   "type": "mobile", 
//		                   "value": "+917032555311", 
//		                   "metadata": {
//		                     "source": {
//		                       "type": "CONTACT", 
//		                       "id": "5bc2a4cb08416dfc"
//		                     }, 
//		                     "primary": true, 
//		                     "sourcePrimary": true
//		                   }
//		                 }
//			]
		
		
		HashSet<String> refMobileNumbers = new HashSet<>();
		for(ContactMobileNumbersObj n : refNumbers ) {
			refMobileNumbers.add(n.getMobileNumber());
		}
		for(JsonElement number : numbersRecievedFromGoogle) {
			JsonObject num = (JsonObject) number;
			if(!refMobileNumbers.contains(num.get("value").getAsString())) {
				addNumber(refNumbers.get(0).getContactId(), num.get("value").getAsString());
				
			}else {
				refMobileNumbers.remove(num.get("value").getAsString());
			}
		}
		
		if(!refMobileNumbers.isEmpty()) {
			for(String 	n : refMobileNumbers) {
				deleteNumber(refNumbers.get(0).getContactId(), n);
			}
		}
	}
	
	private void handleEmails(List<ContactMailsObj> refMails, JsonArray mailsRecievedFromGoogle) {
//		 "emailAddresses": [
//		                    {
//		                      "value": "karthikeya.00004@gmail.com", 
//		                      "metadata": {
//		                        "source": {
//		                          "type": "ACCOUNT", 
//		                          "id": "100549746507535747515"
//		                        }, 
//		                        "verified": true, 
//		                        "primary": true, 
//		                        "sourcePrimary": true
//		                      }
//		                    }
//		                  ]
		HashSet<String> refMailAddresses = new HashSet<String>();
		for(ContactMailsObj m : refMails) {
			refMailAddresses.add(m.getEmail());
		}
		
		for(JsonElement number : mailsRecievedFromGoogle) {
			JsonObject num = (JsonObject) number;
			if(!refMailAddresses.contains(num.get("value").getAsString())) {
				addEmail(refMails.get(0).getContactId(), num.get("value").getAsString());
				
			}else {
				refMailAddresses.remove(num.get("value").getAsString());
			}
		}
		
		if(!refMailAddresses.isEmpty()) {
			for(String n : refMailAddresses) {
				deleteEmail(refMails.get(0).getContactId(), n);
			}
		}
	}
	
	private void addNumber(Integer contactId,String number) {
		ContactMobileNumbersDao dao  = new ContactMobileNumbersDao();
		try {
			dao.addNumberToContactId(contactId, number);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deleteNumber(Integer contactId, String number) {
		ContactMobileNumbersDao dao  = new ContactMobileNumbersDao();
		try {
			dao.deleteNumberWithContactId(contactId, number);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void addEmail(Integer contactId, String email) {
		ContactMailsDao dao = new ContactMailsDao();
		try {
			dao.addMailToContact(contactId, email);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void deleteEmail(Integer contactId, String email) {
		ContactMailsDao dao = new ContactMailsDao();
		try {
			dao.deleteMailForContact(contactId, email);
		} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

	private void deleteContact(HashMap<String, Long> deletedContacts) throws QueryException {
		for (Map.Entry<String, Long> contact : deletedContacts.entrySet()) {
			Delete d = new Delete();
			d.table(Table.Contacts).condition(Contacts.REF_ID, Operators.Equals, contact.getKey());
			d.executeUpdate();
			
		}
	}
	
	

}
