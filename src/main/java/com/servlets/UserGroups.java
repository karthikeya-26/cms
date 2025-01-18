package com.servlets;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.dao.DaoException;
import com.dao.UserGroupsDao;
import com.dbObjects.UserGroupsObj;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.loggers.AppLogger;

@WebServlet("/usergroups")
public class UserGroups extends HttpServlet {
	
	private static AppLogger logger = new AppLogger(UserGroups.class.getName());
    private static final long serialVersionUID = 1L;

    private final UserGroupsDao groupsDao = new UserGroupsDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        JsonObject responseJson = new JsonObject();

        try {
            int userId = getUserId(); // Simulate fetching userId
            List<UserGroupsObj> userGroups = groupsDao.getUserGroups(userId);

            if (userGroups == null || userGroups.isEmpty()) {
                responseJson.addProperty("message", "No user groups found");
            } else {
                responseJson.add("groups", new Gson().toJsonTree(userGroups));
            }
            response.getWriter().print(responseJson);
        } catch (DaoException e) {
        	logger.log(Level.SEVERE, e.getMessage(),e);
        	response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Some error occurred, try again later.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        JsonObject responseJson = new JsonObject();

        try {
            JsonObject requestBody = JsonParser.parseReader(request.getReader()).getAsJsonObject();
            String groupName = requestBody.get("groupName").getAsString();

            if (groupName == null || groupName.trim().isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid group name");
                return;
            }

            int userId = getUserId(); // Simulate fetching userId
            groupsDao.addGroupToUser(userId, groupName);

            responseJson.addProperty("message", "Group added successfully.");
            response.getWriter().print(responseJson);
        } catch (DaoException e) {
        	logger.log(Level.SEVERE, e.getMessage(),e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to add group, please try again later.");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request payload");
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        JsonObject responseJson = new JsonObject();

        try {
            JsonObject requestPayload = JsonParser.parseReader(request.getReader()).getAsJsonObject();
            int groupId = parseGroupId(requestPayload);
            String groupName = requestPayload.get("groupName").getAsString();

            if (groupName == null || groupName.trim().isEmpty()) {
                responseJson.addProperty("message", "Provide a valid group name");
                response.getWriter().print(responseJson);
                return;
            }

            int userId = getUserId(); // Simulate fetching userId
            groupsDao.updateGroupName(userId, groupId, groupName);

            responseJson.addProperty("message", "Group updated successfully.");
            response.getWriter().print(responseJson);
        } catch (DaoException e) {
        	logger.log(Level.SEVERE, e.getMessage(),e);
            responseJson.addProperty("message", e.getMessage());
            response.getWriter().print(responseJson);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request payload");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        JsonObject responseJson = new JsonObject();

        try {
            JsonObject requestPayload = JsonParser.parseReader(request.getReader()).getAsJsonObject();
            int groupId = parseGroupId(requestPayload);

            int userId = getUserId(); // Simulate fetching userId
            groupsDao.deleteGroupForUser(userId, groupId);

            responseJson.addProperty("message", "Group deleted successfully.");
            response.getWriter().print(responseJson);
        } catch (DaoException e) {
        	logger.log(Level.SEVERE, e.getMessage(),e);
            responseJson.addProperty("message", "Group doesn't belong to the user");
            response.getWriter().print(responseJson);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid request payload");
        }
    }

    private int getUserId() {
        // Simulate retrieving the userId (hardcoded for now)
        return 1;
    }

    private int parseGroupId(JsonObject payload) throws IllegalArgumentException {
        try {
            return payload.get("groupId").getAsInt();
        } catch (IllegalStateException | NumberFormatException e) {
            throw new IllegalArgumentException("Provide a valid group ID");
        }
    }
}
