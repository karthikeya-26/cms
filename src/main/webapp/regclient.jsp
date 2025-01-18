<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Register OAuth Client</title>
<style>
    body {
        font-family: Arial, sans-serif;
        margin: 20px;
    }
    form {
        max-width: 600px;
        margin: 0 auto;
    }
    input, select, button {
        width: 100%;
        padding: 10px;
        margin: 10px 0;
        box-sizing: border-box;
    }
    label {
        font-weight: bold;
        margin-bottom: 5px;
        display: inline-block;
    }
    .checkbox-container {
        display: flex;
        flex-direction: column;
        margin-top: 10px;
    }
    .checkbox-container label {
        font-weight: normal;
        margin: 0;
    }
    .add-button {
        width: auto;
        margin: 5px 0;
    }
</style>
</head>
<body>
    <h3>Register OAuth Client</h3>
    
    <form action="api/v1/regclient" method="post">
        <!-- Application Type -->
        <label for="appType">Application Type</label>
        <select name="appType" id="appType" required>
            <option value="web_application">Web Application</option>
            <option value="desktop_application">Desktop Application</option>
            <option value="mobile_application">Mobile Application</option>
            <option value="service_account">Service Account</option>
        </select>

        <!-- App Name -->
        <label for="appName">Name</label>
        <input type="text" id="appName" name="appName" placeholder="Enter your app name" required>

        <!-- Authorized Redirect URIs -->
        <label for="redirectUris">Authorized Redirect URIs</label>
        <div id="redirectUrisContainer">
            <input type="url" name="redirectUris[]" placeholder="Enter a redirect URI" required>
        </div>
        <button type="button" class="add-button" onclick="addField('redirectUrisContainer', 'redirectUris[]', 5)">+ Add URI</button>

        <!-- Scopes --><br>
        <label>Scopes</label>
        <div class="checkbox-container">
            <label><input id="profile-scope" type="checkbox" name="scopes" value="profile"> Profile</label>
            <label><input id="profile-readscope" type="checkbox" name="scopes" value="profile.read"> Profile Read</label>
            <label><input id="contacts-scope" type="checkbox" name="scopes" value="contacts"> Contacts</label>
            <label><input id="contacts-readscope" type="checkbox" name="scopes" value="contacts.read"> Contacts Read</label>
        </div>

        <!-- Submit -->
        <button type="submit">Create</button>
    </form>

    <script>
        function addField(containerId, inputName, maxFields) {
            const container = document.getElementById(containerId);
            if (container.children.length >= maxFields) {
                alert('You can only add up to ' + maxFields + ' URIs.');
                return;
            }
            const input = document.createElement('input');
            input.type = 'url';
            input.name = inputName;
            input.placeholder = 'Enter a URI';
            input.required = true;
            container.appendChild(input);
        }
    </script>
</body>
</html>
