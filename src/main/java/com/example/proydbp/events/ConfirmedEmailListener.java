package com.example.proydbp.events;

import com.example.proydbp.events.domain.EmailSenderService;
import com.example.proydbp.events.email_event.*;
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
    public ConfirmedEmailListener(EmailSenderService emailSenderService) {
        this.emailSenderService = emailSenderService;
    }

    @EventListener
    @Async
    public void handleEmailEvent(SendConfirmedEmailEvent event) throws MessagingException, IOException {
        emailSenderService.sendEmail(event.getMail());
    }

    @EventListener
    @Async
    public void ReviewMeseroCreatedEvent(ReviewMeseroCreatedEvent event) throws MessagingException, IOException {
        emailSenderService.sendEmail(event.getMail());
    }

    @EventListener
    @Async
    public void ReviewMeseroDeletedEvent(ReviewMeseroDeletedEvent event) throws MessagingException, IOException {
        emailSenderService.sendEmail(event.getMail());
    }

    @EventListener
    @Async
    public void ReviewMeseroUpdatedEvent(ReviewMeseroUpdatedEvent event) throws MessagingException, IOException {
        emailSenderService.sendEmail(event.getMail());
    }

    @EventListener
    @Async
    public void handlePedidoLocalCreated(PedidoLocalCreatedEvent event) throws MessagingException, IOException {
        emailSenderService.sendEmail(event.getMail());
    }

    @EventListener
    @Async
    public void handlePedidoLocalUpdatedEvent(PedidoLocalUpdatedEvent event) throws MessagingException, IOException {
        emailSenderService.sendEmail(event.getMail());
    }

    @EventListener
    @Async
    public void handlePedidoLocalDeletedEvent(PedidoLocalDeletedEvent event) throws MessagingException, IOException {
        emailSenderService.sendEmail(event.getMail());
    }

    @EventListener
    @Async
    public void handleEstadoPedidoLocalPreparandoEvent(EstadoPedidoLocalPreparandoEvent event) throws MessagingException, IOException{
        emailSenderService.sendEmail(event.getMail());
    }

    @EventListener
    @Async
    public void handleEstadoPedidoLocalListoEvent(EstadoPedidoLocalListoEvent event) throws MessagingException, IOException{
        emailSenderService.sendEmail(event.getMail());
    }

}
