package com.example.proydbp.events.email_event;

import com.example.proydbp.events.model.Mail;
import com.example.proydbp.reservation.domain.Reservation;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ReservationDeletedEvent extends ApplicationEvent {
    private final Reservation reservation;
    private final Mail mail;

    public ReservationDeletedEvent(Reservation reservation, String recipientEmail) {
        super(reservation);
        this.reservation = reservation;

        Map<String, Object> properties = new HashMap<>();
        properties.put("ID de la Reserva", reservation.getId());
        properties.put("Cliente", reservation.getClient().getEmail());
        properties.put("Mesa", reservation.getTable().getNumero());
        properties.put("Fecha", reservation.getReservationDate());
        properties.put("Hora", reservation.getReservationTime());
        properties.put("Estado", reservation.getStatusReservation());

        Mail mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(recipientEmail)
                .htmlTemplate(new Mail.HtmlTemplate("ReservationDeletedTemplate", properties))
                .subject("Reserva Eliminada")
                .build();

        this.mail = mail;
    }
}