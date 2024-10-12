package com.example.proydbp.events.email_event;

import com.example.proydbp.events.model.Mail;
import com.example.proydbp.reviewMesero.domain.ReviewMesero;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ReviewMeseroUpdatedEvent extends ApplicationEvent {

    private final ReviewMesero reviewMesero;
    private final Mail mail;

    public ReviewMeseroUpdatedEvent(ReviewMesero reviewMesero, String recipientEmail) {
        super(reviewMesero);
        this.reviewMesero = reviewMesero;

        // Configuración del correo electrónico
        Map<String, Object> properties = new HashMap<>();
        properties.put("Nombre de Mesero", reviewMesero.getMesero().getFirstName() + " " + reviewMesero.getMesero().getLastName());
        properties.put("Nueva Puntuacion", reviewMesero.getRatingScore());
        properties.put("Nuevo Comentario", reviewMesero.getComment());

        Mail mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(recipientEmail)
                .htmlTemplate(new Mail.HtmlTemplate("ReviewUpdatedTemplate", properties))
                .subject("Reseña de Mesero Actualizada")
                .build();

        this.mail = mail;
    }
}
