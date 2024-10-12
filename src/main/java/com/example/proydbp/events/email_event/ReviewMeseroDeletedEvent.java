package com.example.proydbp.events.email_event;

import com.example.proydbp.events.model.Mail;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;

@Getter
public class ReviewMeseroDeletedEvent extends ApplicationEvent {

    final private Long reviewMeseroId;
    final private Mail mail;

    public ReviewMeseroDeletedEvent(Long reviewMeseroId, String recipientEmail) {
        super(reviewMeseroId);
        this.reviewMeseroId = reviewMeseroId;

        // Configuración del correo electrónico
        Mail mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .htmlTemplate(new Mail.HtmlTemplate("ReviewDeletedTemplate", new HashMap<>()))
                .subject("Reseña de Mesero Eliminada")
                .build();

        this.mail = mail;
    }
}
