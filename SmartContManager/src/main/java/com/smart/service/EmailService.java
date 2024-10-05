package com.smart.service;

import java.util.Properties;

import org.springframework.stereotype.Service;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

	public boolean sendEmail(String subject, String message, String to) {
		boolean flag = false;
		String from = "anurag.agarwal1102@gmail.com";
		String host = "smtp.gmail.com";
		 Properties properties = System.getProperties();
		 //host set
		 properties.put("mail.smtp.host", host);
		 properties.put("mail.smtp.port", 587);
		 properties.put("mail.smtp.starttls.enable", "true");
		 properties.put("mail.smtp.auth", "true");
		 
		 //step 1: to get the session object
		 Session session = Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("anurag.agarwal1102@gmail.com", "afxsczizvwzukbia");
			}
		 });
		 session.setDebug(true);
		 
		 //step 2: compose the message [text, multi media]
		 MimeMessage m = new MimeMessage(session);
		 try {
			 m.setFrom(from);
			 m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			 
			 //adding subject to message
			 m.setSubject(subject);
//			 m.setText(message);
			 m.setContent(message, "text/html");
			 
			 //Step 3: send the message using Transport class
			 Transport.send(m);
			 flag = true;
		 }
		 catch(Exception e) {
			 e.printStackTrace();
		 }
		 return flag;
	}
}
