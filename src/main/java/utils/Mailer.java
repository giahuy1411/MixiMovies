package utils;

import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class Mailer {
	public static void send(String from, String to, String subject, String body) {
		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.ssl.protocols", "TLSv1.2"); // BẮT BUỘC

		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication("huyncgty00141@gmail.com", "doad zxns kteg jluh");
			}
		});

		try {
			MimeMessage mail = new MimeMessage(session);
			mail.setFrom(new InternetAddress(from));
			mail.setRecipients(Message.RecipientType.TO, to);
			mail.setSubject(subject, "UTF-8");
			mail.setContent(body, "text/html; charset=UTF-8");

			Transport.send(mail);
			System.out.println("Sent!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}