package com.servlets;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.dao.*;
import com.dto.Contact;
import com.dao.DaoException;
import com.dbObjects.ContactMailsObj;
import com.dbObjects.ContactMobileNumbersObj;
import com.dbObjects.ContactsObj;
import com.dbObjects.UserGroupsObj;
import com.filters.SessionFilter;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;
import java.util.regex.Pattern;

@WebServlet("/contacts")
public class Contacts extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Pattern PHONE_PATTERN = Pattern.compile("^\\+?[0-9]{10,15}$");
	private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");
	private static final String ERROR_MESSAGE = "Request could not be processed";
	private final ContactsDao dao = new ContactsDao();
	private final ContactMobileNumbersDao numbersDao = new ContactMobileNumbersDao();
	private final ContactMailsDao mailsDao = new ContactMailsDao();
	private final UserGroupsDao userGroupsDao = new UserGroupsDao();
	private final Gson gson = new Gson();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			int userId = SessionFilter.USER_ID.get();
			String contactId = request.getParameter("contactId");
			JsonObject resJson = new JsonObject();

			if (contactId != null && !contactId.isEmpty()) {
				handleSingleContact(contactId, resJson, response);
			} else {
				handleAllContacts(userId, resJson, response);
			}
		} catch (Exception e) {
			sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			JsonObject contact = parseRequestBody(request);
			System.out.println(contact);

			JsonArray numbers = contact.get("phoneNumbers").getAsJsonArray();
			JsonArray emails = contact.get("emails").getAsJsonArray();

			if (!validateContact(contact, response)) {
				return;
			}
			System.out.println("contact got validated");
			int contactId = dao.addContact(contact.get("firstName").getAsString(),
					contact.get("lastName").getAsString(), SessionFilter.USER_ID.get());
			System.out.println(contactId);
			for (JsonElement number : numbers) {
				String num = number.getAsString();
				System.out.println(num);
				numbersDao.addNumberToContactId(contactId, num);
			}
			for (JsonElement mail : emails) {
				String mai = mail.getAsString();
				System.out.println(mai);
				mailsDao.addMailToContact(contactId, mai);
			}
			sendSuccessResponse(response, "Operation completed");
		} catch (Exception e) {
			System.out.println("exception");
			sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	@Override
	protected void doPut(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			JsonObject contact = parseRequestBody(request);
			if (!validateContactForUpdate(contact, response)) {
				return;
			}
			System.out.println(contact);
			
			dao.updateContact(contact.get("contactId").getAsInt(), contact.get("firstName").getAsString(), contact.get("lastName").getAsString());
			JsonArray oldNumbers = contact.get("oldContactNumbers").getAsJsonArray();
			JsonArray newNumbers = contact.get("newContactNumbers").getAsJsonArray();
			JsonArray oldMails = contact.get("oldMails").getAsJsonArray();
			JsonArray newMails = contact.get("newMails").getAsJsonArray();
			
			sendSuccessResponse(response, "Operation completed");
		} catch (Exception e) {
			sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	@Override
	protected void doDelete(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			JsonObject contact = parseRequestBody(request);
			if (!validateContactId(contact, response)) {
				return;
			}
			System.out.println(contact.get("contactId").getAsString());
			dao.deleteContact(Integer.valueOf(contact.get("contactId").getAsString()));
			sendSuccessResponse(response, "Operation completed");
		} catch (Exception e) {
			sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	private JsonObject parseRequestBody(HttpServletRequest request) throws IOException {
		return JsonParser.parseReader(request.getReader()).getAsJsonObject();
	}

	private void handleSingleContact(String contactId, JsonObject resJson, HttpServletResponse response)
			throws IOException {
		try {
			ContactsObj contactDetails = dao.getContactWithId(Integer.valueOf(contactId));
			Contact contact = new Contact();
			contact.contactDetails = contactDetails;
			List<ContactMobileNumbersObj> numbers = numbersDao.getContactMobileNumbers(Integer.valueOf(contactId));
			List<ContactMailsObj> mails = mailsDao.getMailsWithContactId(Integer.valueOf(contactId));
			List<UserGroupsObj> groups = userGroupsDao.getContactGroups(Integer.valueOf(contactId));
			if (!numbers.isEmpty()) {
				contact.numbers = numbers;
			}
			if (!mails.isEmpty()) {
				contact.mails = mails;
			}
			if (!groups.isEmpty()) {
				contact.groups = groups;
			}
			resJson.add("contact", gson.toJsonTree(contact));
			sendJsonResponse(response, resJson);
		} catch (Exception e) {
			sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST);
		}
	}

	private void handleAllContacts(int userId, JsonObject resJson, HttpServletResponse response) throws IOException {
		try {
			List<ContactsObj> contacts = dao.getContactsWithUserId(userId);
			resJson.add("contacts", gson.toJsonTree(contacts));

			sendJsonResponse(response, resJson);
		} catch (DaoException e) {
			sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	private boolean validateContact(JsonObject contact, HttpServletResponse response) throws IOException {
		if (!validateBasicFields(contact, response))
			return false;
		if (!validateArrayField(contact, "phoneNumbers", PHONE_PATTERN, response))
			return false;
		if (!validateArrayField(contact, "emails", EMAIL_PATTERN, response))
			return false;
		return validateGroups(contact, response);
	}

	private boolean validateContactForUpdate(JsonObject contact, HttpServletResponse response) throws IOException {
		if (!validateContactId(contact, response))
			return false;
		return validateContact(contact, response);
	}

	private boolean validateBasicFields(JsonObject contact, HttpServletResponse response) throws IOException {
		String firstName = contact.has("firstName") ? contact.get("firstName").getAsString() : null;
		String lastName = contact.has("lastName") ? contact.get("lastName").getAsString() : null;

		if (firstName == null || firstName.isEmpty() || lastName == null || lastName.isEmpty()) {
			sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST);
			return false;
		}
		return true;
	}

	private boolean validateArrayField(JsonObject contact, String fieldName, Pattern pattern,
			HttpServletResponse response) throws IOException {
		JsonArray array = contact.has(fieldName) ? contact.getAsJsonArray(fieldName) : new JsonArray();
		for (int i = 0; i < array.size(); i++) {
			if (!pattern.matcher(array.get(i).getAsString()).matches()) {
				sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST);
				return false;
			}
		}
		return true;
	}

	private boolean validateGroups(JsonObject contact, HttpServletResponse response) throws IOException {
		JsonArray groups = contact.has("groups") ? contact.getAsJsonArray("groups") : new JsonArray();
		try {
			for (int i = 0; i < groups.size(); i++) {
				groups.get(i).getAsInt();
			}
			return true;
		} catch (Exception e) {
			sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST);
			return false;
		}
	}

	private boolean validateContactId(JsonObject contact, HttpServletResponse response) throws IOException {
		try {
			if (!contact.has("contactId") || contact.get("contactId").getAsInt() <= 0) {
				sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST);
				return false;
			}
			return true;
		} catch (Exception e) {
			sendErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST);
			return false;
		}
	}

	private void sendJsonResponse(HttpServletResponse response, JsonObject json) throws IOException {
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().print(json.toString());
	}

	private void sendSuccessResponse(HttpServletResponse response, String message) throws IOException {
		JsonObject json = new JsonObject();
		json.addProperty("status", "success");
		json.addProperty("message", message);
		sendJsonResponse(response, json);
	}

	private void sendErrorResponse(HttpServletResponse response, int statusCode) throws IOException {
		response.setStatus(statusCode);
		JsonObject json = new JsonObject();
		json.addProperty("status", "error");
		json.addProperty("message", ERROR_MESSAGE);
		sendJsonResponse(response, json);
	}
}