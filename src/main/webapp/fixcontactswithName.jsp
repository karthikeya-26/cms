<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.dbObjects.*"%>
<%@ page import="com.session.*"%>
<%@ page import="com.filters.*"%>
<%@ page import="java.util.*"%>
<%@ page import="java.time.*"%>
<%@ page import="com.dao.*"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>

<%
Integer userId = SessionFilter.USER_ID.get();
UserDetailsObj user = SessionDataManager.usersData.get(userId);
ContactsDao dao = new ContactsDao();
List<ContactsObj> userContactsSortedByFirstName = dao.getContactsWithUserIdSorted(userId);
%>
<body>
<%
    int i = 0;
    int contactsLength = userContactsSortedByFirstName.size();

    while (i < contactsLength) {
        String currentFullName = userContactsSortedByFirstName.get(i).getFirstName() + " " + userContactsSortedByFirstName.get(i).getLastName();
        List<Integer> duplicateContactIds = new ArrayList<>();
        duplicateContactIds.add(userContactsSortedByFirstName.get(i).getContactId());

        // Check for duplicates for the current name
        int j = i + 1;
        while (j < contactsLength && 
               (userContactsSortedByFirstName.get(j).getFirstName() + " " + userContactsSortedByFirstName.get(j).getLastName()).equals(currentFullName)) {
            duplicateContactIds.add(userContactsSortedByFirstName.get(j).getContactId());
            j++;
        }

        // If more than one contact with the same name is found, display it
        if (duplicateContactIds.size() > 1) {
%>
        <p>There are <%=duplicateContactIds.size()%> contacts with the same name <%=currentFullName%>:</p>
        <form action="userOp?action=mergeContacts" method="post">
            <%
                for (Integer contactId : duplicateContactIds) {
            %>
                <input type="hidden" name="contactIds" value="<%=contactId%>">
            <%
                }
            %>
            <input type="submit" value="MERGE">
        </form>
<%
        }

        // Move `i` to the next distinct name
        i = j;
    }
%>



</body>
</html>