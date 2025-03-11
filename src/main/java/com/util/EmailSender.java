package com.util;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class EmailSender {

    public static void sendVerificationEmail(String recipient, String verificationLink) throws Exception {
    	System.out.println("hello");
        final String senderEmail = "karthikeya.00004@gmail.com";
        final String senderPassword = "zfbf ywnf wkkf krei";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(senderEmail, senderPassword);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(senderEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject("Verify Your Email");
        message.setText("Click the link to verify your email: " + verificationLink);

        Transport.send(message);
        System.out.println("Sending mail to :"+ recipient);
    }
   
}
