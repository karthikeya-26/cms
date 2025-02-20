package com.loggers;

import org.apache.catalina.valves.AccessLogValve;
import javax.servlet.http.Cookie;
import java.io.CharArrayWriter;
import java.util.Arrays;

public class CustomAccessLog extends AccessLogValve {

    @Override
    public void log(CharArrayWriter message) {
        if (message == null || message.size() == 0) {
            return;
        }

        String originalMessage = message.toString();
        
        // Create a new CharArrayWriter for the modified message
        CharArrayWriter modifiedMessage = new CharArrayWriter();
        modifiedMessage.append(originalMessage);

        // Add your custom logging logic here if needed
        // For example, you could append additional information
        
        // Call the parent class's log method with the modified message
        super.log(modifiedMessage);
    }

    private String formatCookies(Cookie[] cookies) {
        if (cookies == null || cookies.length == 0) {
            return "No Cookies";
        }
        return Arrays.stream(cookies)
                .map(cookie -> cookie.getName() + "=" + cookie.getValue())
                .reduce((c1, c2) -> c1 + "; " + c2)
                .orElse("No Cookies");
    }
}