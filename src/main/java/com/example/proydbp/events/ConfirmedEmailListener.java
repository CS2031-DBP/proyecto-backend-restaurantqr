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

    // ------ PERFIL ------
    @Async
    @EventListener
    public void handleBienvenidaMeseroEvent(BienvenidaMeseroEvent event)throws MessagingException, IOException{
        emailSenderService.sendEmail(event.getMail());
    }

    @Async
    @EventListener
    public void handleBienvenidaClienteEvent(BienvenidaClienteEvent event)throws MessagingException, IOException{
        emailSenderService.sendEmail(event.getMail());
    }

    @Async
    @EventListener
    public void handleBienvenidaRepartidorEvent(BienvenidaRepartidorEvent event)throws MessagingException, IOException{
        emailSenderService.sendEmail(event.getMail());
    }

    @Async
    @EventListener
    public void handleBienvenidaChefEvent(BienvenidaChefEvent event)throws MessagingException, IOException{
        emailSenderService.sendEmail(event.getMail());
    }

    @Async
    @EventListener
    public void handlePerfilUpdateClienteEvent(PerfilUpdateClienteEvent event)throws MessagingException, IOException{
        emailSenderService.sendEmail(event.getMail());
    }

    @Async
    @EventListener
    public void handlePerfilUpdateMeseroEvent(PerfilUpdateMeseroEvent event)throws MessagingException, IOException{
        emailSenderService.sendEmail(event.getMail());
    }

    @Async
    @EventListener
    public void handlePerfilUpdateRepartidorEvent(PerfilUpdateRepartidorEvent event)throws Exception {
        emailSenderService.sendEmail(event.getMail());
    }

    // --------------------

    // ------ DELIVERY ------
    @Async
    @EventListener
    public void handleDeliveryCrearEvent(DeliveryCrearEvent event)throws MessagingException, IOException{
        emailSenderService.sendEmail(event.getMail());
    }

    @Async
    @EventListener
    public void handleDeliveryCrearRepartidorEvent(DeliveryCrearRepartidorEvent event)throws Exception {
        emailSenderService.sendEmail(event.getMail());
    }

    @Async
    @EventListener
    public void handleDeliveryEstadoChangeEvent(DeliveryEstadoChangeEvent event)throws MessagingException, IOException{
        emailSenderService.sendEmail(event.getMail());
    }

    @Async
    @EventListener
    public void handleDeliveryUpdateEvent(DeliveryUpdateEvent event)throws MessagingException, IOException{
        emailSenderService.sendEmail(event.getMail());
    }

    // ----------------------

    // ------ PEDIDO LOCAL ------
    @Async
    @EventListener
    public void handlePedidoLocalCrearEvent(PedidoLocalCrearEvent event)throws MessagingException, IOException{
        emailSenderService.sendEmail(event.getMail());
    }

    @Async
    @EventListener
    public void handlePedidoLocalUpdateEvent(PedidoLocalUpdateEvent event)throws MessagingException, IOException{
        emailSenderService.sendEmail(event.getMail());
    }

    @Async
    @EventListener
    public void handlePedidoLocalCrearMeseroEvent(PedidoLocalCrearMeseroEvent event)throws MessagingException, IOException{
        emailSenderService.sendEmail(event.getMail());
    }

    @Async
    @EventListener
    public void handlePedidoLocalEstadoChangeEvent(PedidoLocalEstadoChangeEvent event)throws MessagingException, IOException{
        emailSenderService.sendEmail(event.getMail());
    }

    // --------------------------

    // ------ RESERVAS ------
    @Async
    @EventListener
    public void handleReservaCrearEvent(ReservaCrearEvent event)throws MessagingException, IOException{
        emailSenderService.sendEmail(event.getMail());
    }

    @Async
    @EventListener
    public void handleReservaUpdateEvent(ReservaUpdateEvent event)throws MessagingException, IOException{
        emailSenderService.sendEmail(event.getMail());
    }

    @Async
    @EventListener
    public void handleReservaEstadoChangeEvent(ReservaEstadoChangeEvent event)throws MessagingException, IOException{
        emailSenderService.sendEmail(event.getMail());
    }

    // ----------------------

    // ------ REVIEW MESERO ------
    @Async
    @EventListener
    public void handleReviewMeseroCreadoEvent(ReviewMeseroCreadoEvent event)throws MessagingException, IOException{
        emailSenderService.sendEmail(event.getMail());
    }

    @Async
    @EventListener
    public void handleReviewMeseroDeleteEvent(ReviewMeseroDeleteEvent event)throws MessagingException, IOException{
        emailSenderService.sendEmail(event.getMail());
    }

    // ---------------------------

    // ------ REVIEW DELIVERY ------
    @Async
    @EventListener
    public void handleReviewDeliveryCreadoEvent(ReviewDeliveryCreadoEvent event)throws MessagingException, IOException{
        emailSenderService.sendEmail(event.getMail());
    }

    @Async
    @EventListener
    public void handleReviewDeliveryDeleteEvent(ReviewDeliveryDeleteEvent event)throws MessagingException, IOException{
        emailSenderService.sendEmail(event.getMail());
    }

    // -----------------------------
}
