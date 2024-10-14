package com.example.proydbp.events.email_event;

import com.example.proydbp.events.model.Mail;
import com.example.proydbp.reviewMesero.domain.ReviewMesero;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ReviewMeseroCreatedEvent extends ApplicationEvent {

    final private ReviewMesero reviewMesero;
    final private Mail mail;

    public ReviewMeseroCreatedEvent(ReviewMesero reviewMesero, String recipientEmail) {
        super(reviewMesero);
        this.reviewMesero = reviewMesero;

        // Configuración del correo electrónico
        Map<String, Object> properties = new HashMap<>();
        properties.put("Nombre de Mesero", reviewMesero.getMesero().getFirstName() + " " + reviewMesero.getMesero().getLastName());
        properties.put("Puntuacion", reviewMesero.getRatingScore());
        properties.put("Comentario", reviewMesero.getComment());

        Mail mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(recipientEmail)
                .htmlTemplate(new Mail.HtmlTemplate("ReviewMeseroCreatedTemplate", properties))
                .subject("Nueva Reseña de Mesero Creada")
                .build();

        this.mail = mail;
    }

}
