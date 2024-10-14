package com.example.proydbp.events.email_event;

import com.example.proydbp.reservation.domain.Reservation;
import com.example.proydbp.events.model.Mail;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class MyReservationUpdatedEvent extends ApplicationEvent {

    private final Reservation reservation;
    private final Mail mail;

    public MyReservationUpdatedEvent(Reservation reservation, String recipientEmail) {
        super(reservation);
        this.reservation = reservation;

        // Configuración del correo electrónico
        Map<String, Object> properties = new HashMap<>();
        properties.put("ID de la Reserva", reservation.getId());
        properties.put("Cliente", reservation.getClient().getFirstName() + " " + reservation.getClient().getLastName());
        properties.put("Mesa", reservation.getMesa().getNumero());
        properties.put("Número de Personas", reservation.getNumOfPeople());
        properties.put("Estado", reservation.getStatusReservation());

        Mail mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(recipientEmail)
                .htmlTemplate(new Mail.HtmlTemplate("MyReservationUpdatedTemplate", properties))
                .subject("Mi Reserva Actualizada")
                .build();

        this.mail = mail;
    }
}
