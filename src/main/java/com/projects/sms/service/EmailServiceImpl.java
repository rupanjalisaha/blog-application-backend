package com.projects.sms.service;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

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

		System.out.println("API KEY: " + System.getenv("SENDGRID_API_KEY"));
		System.out.println("FROM EMAIL: " + System.getenv("SENDGRID_FROM_EMAIL"));
        String apiKey = System.getenv("SENDGRID_API_KEY");

        SendGrid sg = new SendGrid(apiKey);

        Email from = new Email(System.getenv("SENDGRID_FROM_EMAIL"));
        Email to = new Email(toEmail);

        String subject = "Verify your email for uvbportal account";
        
        String encodedToken = URLEncoder.encode(token, StandardCharsets.UTF_8);
        String verificationUrl = baseUrl +"/verify?token=" + encodedToken;
        String htmlContent =
        	    "<!DOCTYPE html>" +
        	    "<html>" +
        	    "<body style='font-family: Arial, sans-serif; background-color:#f4f4f4; padding:20px;'>" +

        	    "<div style='max-width:600px; margin:auto; background:#ffffff; padding:20px; border-radius:8px; text-align:center;'>" +

        	    "<h2 style='color:#333;'>Email Verification</h2>" +

        	    "<p style='color:#555;'>Thanks for registering.</p>" +
        	    "<p style='color:#555;'>Please verify your email by clicking the button below:</p>" +

        	    "<a href='" + verificationUrl + "' " +
        	    "style='display:inline-block; padding:12px 20px; margin-top:15px; " +
        	    "background-color:#007bff; color:#ffffff; text-decoration:none; border-radius:5px;'>" +
        	    "Verify Email</a>" +

        	    "<p style='margin-top:20px; font-size:12px; color:#999;'>If you didn’t request this, you can ignore this email.</p>" +

        	    "</div>" +
        	    "</body>" +
        	    "</html>";

        Content content = new Content("text/html",htmlContent);


        Mail mail = new Mail(from, subject, to, content);
        

        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);
            System.out.println("Status Code: " + response.getStatusCode());

        } catch (IOException ex) {
            throw new RuntimeException("Email sending failed", ex);
        }
    }

}
	
