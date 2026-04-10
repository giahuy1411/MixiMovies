package utils;

import java.io.InputStream;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.*;

public class Mailer {

	private static String mailUsername;
	private static String mailPassword;

	static {
		try {
			Properties env = new Properties();
			InputStream is = Mailer.class.getClassLoader().getResourceAsStream("env.properties");
			if (is != null) {
				env.load(is);
				is.close();
				mailUsername = env.getProperty("MAIL_USERNAME", "");
				mailPassword = env.getProperty("MAIL_PASSWORD", "");
			} else {
				System.out.println("⚠️ Mailer: Không tìm thấy env.properties, chức năng gửi mail sẽ không hoạt động.");
				mailUsername = "";
				mailPassword = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			mailUsername = "";
			mailPassword = "";
		}
	}

	public static void send(String from, String to, String subject, String body) {
		if (mailUsername.isEmpty() || mailPassword.isEmpty()) {
			System.out.println("⚠️ Mailer: Chưa cấu hình MAIL_USERNAME / MAIL_PASSWORD trong env.properties");
			return;
		}

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.ssl.protocols", "TLSv1.2");

		Session session = Session.getInstance(props, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(mailUsername, mailPassword);
			}
		});

		try {
			MimeMessage mail = new MimeMessage(session);
			mail.setFrom(new InternetAddress(from));
			mail.setRecipients(Message.RecipientType.TO, to);
			mail.setSubject(subject, "UTF-8");
			mail.setContent(body, "text/html; charset=UTF-8");

			Transport.send(mail);
			System.out.println("✅ Email sent to: " + to);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}