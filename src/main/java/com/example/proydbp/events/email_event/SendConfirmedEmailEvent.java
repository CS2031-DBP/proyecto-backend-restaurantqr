package com.example.proydbp.events.email_event;

import com.example.proydbp.cliente.domain.Client;
import com.example.proydbp.events.model.Mail;
import com.example.proydbp.reservation.domain.Reservation;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class SendConfirmedEmailEvent extends ApplicationEvent {

    final private Mail mail;

    public SendConfirmedEmailEvent(Reservation reservation) {
        super(reservation);

        Client client = reservation.getClient();

        Map<String, Object> properties = new HashMap<>();

        Mail mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(client.getEmail())
                .htmlTemplate(new Mail.HtmlTemplate("EmailTemplate", properties))
                .subject("Confirmación de su Reserva")
                .build();

        this.mail = mail;
    }
}
