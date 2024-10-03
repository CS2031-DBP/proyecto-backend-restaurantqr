package com.example.proydbp.events;

import com.example.proydbp.events.domain.EmailSenderService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ConfirmedEmailListener {

    final private EmailSenderService emailSenderService;

    @Autowired
    public ConfirmedEmailListener(EmailSenderService emailSenderService) {this.emailSenderService = emailSenderService;}

    @EventListener
    @Async
    public void handleEmailEvent(SendConfirmedEmailEvent event) throws MessagingException, IOException {
        emailSenderService.sendEmail(event.getMail());
    }
}
