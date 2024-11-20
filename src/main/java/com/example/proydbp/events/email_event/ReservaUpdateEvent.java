package com.example.proydbp.events.email_event;

import com.example.proydbp.events.model.Mail;
import com.example.proydbp.reservation.domain.Reservation;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ReservaUpdateEvent extends ApplicationEvent {

    final private Mail mail;
    final private Reservation reservation;

    public ReservaUpdateEvent(Reservation reservation, String recipientEmail){
        super(reservation);
        this.reservation = reservation;

        Map<String, Object> properties = new HashMap<>();
        properties.put("descripcionReserva", reservation.getDescripcion());
        properties.put("fechaReserva", reservation.getFecha().toLocalDate());
        properties.put("horaReserva", reservation.getFecha().toLocalTime());

        this.mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(recipientEmail)
                .htmlTemplate(new Mail.HtmlTemplate("ReservaUpdateTemplate", properties))
                .subject("Actualizamos su reserva")
                .build();
    }
}
