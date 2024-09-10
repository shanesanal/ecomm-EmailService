package com.ecommerce.emailservice.Consumer;

import com.ecommerce.emailservice.dto.SendEmailDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Service

public class SendEmailEventConsumer {

    private ObjectMapper objectMapper;

    public SendEmailEventConsumer(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }
    @KafkaListener(topics = "send-email", groupId = "emailservice")
    public void handleSendEmailEvent(String message) throws JsonProcessingException {
        SendEmailDto sendEmailDto= objectMapper.readValue(message, SendEmailDto.class);

        String to= sendEmailDto.getTo();
        String from= sendEmailDto.getFrom();
        String subject= sendEmailDto.getSubject();
        String body= sendEmailDto.getBody();

        System.out.println("Sending email to "+to);

        System.out.println("TLSEmail Start");        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); //SMTP Host
        props.put("mail.smtp.port", "587"); //TLS Port
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS


        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, "password");
            }
        };
        Session session = Session.getInstance(props, auth);

        EmailUtil.sendEmail(session, to, subject, body);

    }
}
