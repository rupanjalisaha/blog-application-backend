package com.projects.sms.service;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

;@Service

public class EmailServiceImpl implements EmailService {

	
	@Value("${app.base-url}")
	private String baseUrl; // e.g. http://localhost:3000
	
	@Override
    public void sendVerificationEmail(String toEmail, String token) {

        String apiKey = System.getenv("SENDGRID_API_KEY");

        SendGrid sg = new SendGrid(apiKey);

        Email from = new Email(System.getenv("SENDGRID_FROM_EMAIL"));
        Email to = new Email(toEmail);

        String subject = "Verify your email for uvbportal account";

        String contentText =
                "Click to verify: " + baseUrl+"/verify?token=" + token;

        Content content = new Content("text/plain", contentText);

        Mail mail = new Mail(from, subject, to, content);

        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            sg.api(request);

        } catch (IOException ex) {
            throw new RuntimeException("Email sending failed", ex);
        }
    }

}
	
