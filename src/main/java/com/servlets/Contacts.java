package com.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.ContactsDao;
import com.dao.DaoException;
import com.dbObjects.ContactsObj;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.session.SessionDataManager;

/**
 * Servlet implementation class Contacts
 */
@WebServlet("/contacts")
public class Contacts extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ContactsDao dao = new ContactsDao();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Contacts() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
	
//		int userId = SessionDataManager.getUserwithId(request);
		int userId =1;
		String contactId = request.getParameter("contactId");
		System.out.println("req param contactId :"+ contactId);
		System.out.println("ConatctId : "+request.getInputStream().read());
		response.setContentType("application/json");
		JsonObject resJson = new JsonObject();
		//session_id
		if(contactId!= null && !contactId.isEmpty()) {
			ContactsObj contact = null;
			try {
				contact = dao.getContactWithId(Integer.valueOf(contactId));
			} catch (NumberFormatException  e) {
				resJson.addProperty("status", 400);
				resJson.addProperty("message", "not a valid contact Id");
				response.getWriter().print(resJson.toString());
				return;
			} catch( DaoException e) {
				resJson.addProperty("status", 500);
				resJson.addProperty("message", "Something went wrong, please try again later");
				response.getWriter().print(resJson.toString());
				return;
			}
			
			resJson.addProperty("status", 200);
			resJson.addProperty("message", "Contact fetched successfully");
			resJson.addProperty("contact", new Gson().toJson(contact));
			response.getWriter().print(resJson);
			
		}else {
			List<ContactsObj> contacts = null;
			try {
				contacts = dao.getContactsWithUserId(userId);
			} catch (DaoException e) {
				response.sendError(500, "Try again later");
			}
			resJson.addProperty("contacts", new Gson().toJson(contacts));
			response.getWriter().print(resJson.toString());
			response.getWriter().flush();
			return;
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    BufferedReader inputReader = request.getReader();
	    StringBuilder inputString = new StringBuilder();
	    String line;
	    
	    response.setContentType("application/json");
	    PrintWriter out = response.getWriter();

	    while ((line = inputReader.readLine()) != null) {
	        inputString.append(line);
	    }
	    System.out.println(inputString.toString());
	    // Convert inputString to a JSON object
	    JsonObject contact = JsonParser.parseString(inputString.toString()).getAsJsonObject();

	    // Extract fields
	    String firstName = contact.has("firstName") ? contact.get("firstName").getAsString() : null;
	    String lastName = contact.has("lastName") ? contact.get("lastName").getAsString() : null;
        JsonArray phoneNumbers = contact.has("phoneNumbers") ? contact.getAsJsonArray("phoneNumbers") : new JsonArray();
        JsonArray emailAddresses = contact.has("emailAddresses") ? contact.getAsJsonArray("emailAddresses") : new JsonArray();
	    JsonArray groups = contact.has("groups") ? contact.getAsJsonArray("groups") : new JsonArray();

	    // Validation patterns
	    Pattern phonePattern = Pattern.compile("^\\+?[0-9]{10,15}$"); // Valid phone numbers
	    Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"); // Valid emails

	    // Validate firstName
	    if (firstName == null) {
	        response.sendError(400, "First Name can't be empty");
	        return;
	    }

	    // Validate lastName
	    if (lastName == null) {
	        response.sendError(400, "Last Name can't be empty");
	        return;
	    }

	    // Validate phoneNumbers
	    for (int i = 0; i < phoneNumbers.size(); i++) {
	        String phone = phoneNumbers.get(i).getAsString();
	        if (!phonePattern.matcher(phone).matches()) {
	            response.sendError(400, "Invalid phone number: " + phone);
	            return;
	        }
	    }

	    // Validate emailAddresses
	    for (int i = 0; i < emailAddresses.size(); i++) {
	        String email = emailAddresses.get(i).getAsString();
	        if (!emailPattern.matcher(email).matches()) {
	            response.sendError(400, "Invalid email address: " + email);
	            return;
	        }
	    }

	    // Validate groups
	    for (int i = 0; i < groups.size(); i++) {
	        try {
	            groups.get(i).getAsInt(); // Check if the value is an integer
	        } catch (NumberFormatException | IllegalStateException e) {
	            response.sendError(400, "Invalid group ID: " + groups.get(i));
	            return;
	        }
	    }

	    // If all validations pass
	    JsonObject responseJson = new JsonObject();
	    responseJson.addProperty("message", "contact added Successfully");
	    out.print(responseJson);
	    
	}
	
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		BufferedReader inputReader = request.getReader();
	    StringBuilder inputString = new StringBuilder();
	    String line;
	    JsonObject responseJson = new JsonObject();
	    
	    response.setContentType("application/json");
	    PrintWriter out = response.getWriter();

	    while ((line = inputReader.readLine()) != null) {
	        inputString.append(line);
	    }
//	    System.out.println(inputString.toString());
	    // Convert inputString to a JSON object
	    JsonObject contact = JsonParser.parseString(inputString.toString()).getAsJsonObject();

	    // Extract fields
	    int contactId = -1;
	    if (contact.has("contactId")) {
	        try {
	            contactId = contact.get("contactId").getAsInt(); // Directly try to parse as an integer
	        } catch (IllegalStateException e) {
	           
	            try {
	                contactId = Integer.parseInt(contact.get("contactId").getAsString());
	            } catch (NumberFormatException ex) {
	                // Log and keep the default value -1
	               responseJson.addProperty("message", "provie a valid contact");
	            }
	        }
	    }
	    
	    String firstName = contact.has("firstName") ? contact.get("firstName").getAsString() : null;
	    String lastName = contact.has("lastName") ? contact.get("lastName").getAsString() : null;
        JsonArray phoneNumbers = contact.has("phoneNumbers") ? contact.getAsJsonArray("phoneNumbers") : new JsonArray();
        JsonArray emailAddresses = contact.has("emailAddresses") ? contact.getAsJsonArray("emailAddresses") : new JsonArray();
	    JsonArray groups = contact.has("groups") ? contact.getAsJsonArray("groups") : new JsonArray();

	    // Validation patterns
	    Pattern phonePattern = Pattern.compile("^\\+?[0-9]{10,15}$"); // Valid phone numbers
	    Pattern emailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"); // Valid emails

	    if(contactId == -1) {
	    	response.sendError(400, "Provide a valid contactId");
	    	return;
	    }
	    // Validate firstName
	    if (firstName == null || firstName.isEmpty()) {
	        response.sendError(400, "First Name can't be empty");
	        return;
	    }

	    // Validate lastName
	    if (lastName == null || lastName.isEmpty()) {
	        response.sendError(400, "Last Name can't be empty");
	        return;
	    }

	    // Validate phoneNumbers
	    for (int i = 0; i < phoneNumbers.size(); i++) {
	        String phone = phoneNumbers.get(i).getAsString();
	        if (!phonePattern.matcher(phone).matches()) {
	            response.sendError(400, "Invalid phone number: " + phone);
	            return;
	        }
	    }

	    // Validate emailAddresses
	    for (int i = 0; i < emailAddresses.size(); i++) {
	        String email = emailAddresses.get(i).getAsString();
	        if (!emailPattern.matcher(email).matches()) {
	            response.sendError(400, "Invalid email address: " + email);
	            return;
	        }
	    }

	    // Validate groups
	    for (int i = 0; i < groups.size(); i++) {
	        try {
	            groups.get(i).getAsInt(); // Check if the value is an integer
	        } catch (NumberFormatException | IllegalStateException e) {
	            response.sendError(400, "Invalid group ID: " + groups.get(i));
	            return;
	        }
	    }

	    // If all validations pass
	    
	    responseJson.addProperty("message", "contact updated Successfully");
	    out.print(responseJson);
	}
	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException , IOException {
		BufferedReader inputReader = request.getReader();
	    StringBuilder inputString = new StringBuilder();
	    String line;
	    
	    response.setContentType("application/json");
	    PrintWriter out = response.getWriter();

	    while ((line = inputReader.readLine()) != null) {
	        inputString.append(line);
	    }

	    // Convert inputString to a JSON object
	    JsonObject contact = JsonParser.parseString(inputString.toString()).getAsJsonObject();
	    
	    if(contact.size()<= 0 || contact.size()>1) {
	    	response.sendError(400,"Invalid data format recieved");
	    	return;
	    }
	    
	    int contactId ;
	    try {
	    	contactId = contact.get("contactId").getAsInt();
	    }catch(NumberFormatException e) {
	    	response.sendError(400,"Invalid contactId format recieved ");
	    }
	    
	    JsonObject jsonResponse = new JsonObject();
	    jsonResponse.addProperty("message", "contact delted successfully");
	    
	    
	    out.write(jsonResponse.toString());
	    
	}

}
