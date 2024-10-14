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

    // ---- RESERVATION ----
    @EventListener
    @Async
    public void handleReservationCreatedEvent(ReservationCreatedEvent event) throws MessagingException, IOException {
        emailSenderService.sendEmail(event.getMail());
    }

    @EventListener
    @Async
    public void handleReservationUpdatedEvent(ReservationUpdatedEvent event) throws MessagingException, IOException {
        emailSenderService.sendEmail(event.getMail());
    }

    @EventListener
    @Async
    public void handleReservationDeletedEvent(ReservationDeletedEvent event) throws MessagingException, IOException {
        emailSenderService.sendEmail(event.getMail());
    }

    @EventListener
    @Async
    public void handleReservationFinishedEvent(ReservationFinishedEvent event) throws MessagingException, IOException {
        emailSenderService.sendEmail(event.getMail());
    }

    @EventListener
    @Async
    public void handleReservationCanceladoEvent(ReservationCanceladoEvent event) throws MessagingException, IOException {
        emailSenderService.sendEmail(event.getMail());
    }

    @EventListener
    @Async
    public void handleReservationConfirmedEvent(ReservationConfirmedEvent event) throws MessagingException, IOException {
        emailSenderService.sendEmail(event.getMail());
    }

    @EventListener
    @Async
    public void handleMyReservationUpdatedEvent(MyReservationUpdatedEvent event) throws MessagingException, IOException {
        emailSenderService.sendEmail(event.getMail());
    }

    // ----

    // ---- REVIEW MESERO ----
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

    // ----

    // ---- REVIEW DELIVERY ----
    @EventListener
    @Async
    public void handleReviewDeliveryCreatedEvent(ReviewDeliveryCreatedEvent event) throws MessagingException, IOException {
        emailSenderService.sendEmail(event.getMail());
    }

    @EventListener
    @Async
    public void handleReviewDeliveryDeletedEvent(ReviewDeliveryDeletedEvent event) throws MessagingException, IOException {
        emailSenderService.sendEmail(event.getMail());
    }

    @EventListener
    @Async
    public void handleReviewDeliveryUpdatedEvent(ReviewDeliveryUpdatedEvent event) throws MessagingException, IOException {
        emailSenderService.sendEmail(event.getMail());
    }

    // ----

    // ---- PEDIDO LOCAL ----
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

    // ----

    // ---- DELIVERY ----
    @EventListener
    @Async
    public void handleDeliveryCanceladoEvent(DeliveryCanceladoEvent event) throws MessagingException, IOException {
        emailSenderService.sendEmail(event.getMail());
    }

    @EventListener
    @Async
    public void handleDeliveryCreatedEvent(DeliveryCreatedEvent event) throws MessagingException, IOException {
        emailSenderService.sendEmail(event.getMail());
    }

    @EventListener
    @Async
    public void handleDeliveryDeletedEvent(DeliveryDeletedEvent event) throws MessagingException, IOException {
        emailSenderService.sendEmail(event.getMail());
    }

    @EventListener
    @Async
    public void handleDeliveryEnRutaEvent(DeliveryEnRutaEvent event) throws MessagingException, IOException {
        emailSenderService.sendEmail(event.getMail());
    }

    @EventListener
    @Async
    public void handleDeliveryEntregadoEvent(DeliveryEntregadoEvent event) throws MessagingException, IOException {
        emailSenderService.sendEmail(event.getMail());
    }

    @EventListener
    @Async
    public void handleDeliveryListoEvent(DeliveryListoEvent event) throws MessagingException, IOException {
        emailSenderService.sendEmail(event.getMail());
    }

    @EventListener
    @Async
    public void handleDeliveryPreparandoEvent(DeliveryPreparandoEvent event) throws MessagingException, IOException {
        emailSenderService.sendEmail(event.getMail());
    }

    @EventListener
    @Async
    public void handleDeliveryUpdatedEvent(DeliveryUpdatedEvent event) throws MessagingException, IOException {
        emailSenderService.sendEmail(event.getMail());
    }

    // ----
}
