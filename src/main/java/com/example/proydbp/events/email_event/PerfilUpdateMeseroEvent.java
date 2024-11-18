package com.example.proydbp.events.email_event;

import com.example.proydbp.events.model.Mail;
import com.example.proydbp.mesero.domain.Mesero;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PerfilUpdateMeseroEvent extends ApplicationEvent {

    final private Mail mail;
    final private Mesero mesero;

    public PerfilUpdateMeseroEvent(Mesero mesero, String recipientEmail){
        super(mesero);
        this.mesero = mesero;

        Map<String, Object> properties = new HashMap<>();
        properties.put("firstName", mesero.getFirstName());
        properties.put("lastName", mesero.getLastName());
        properties.put("phone", mesero.getPhoneNumber());
        properties.put("updatedAt", mesero.getUpdatedAt().toString()); // fecha de actualización

        this.mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(recipientEmail)
                .htmlTemplate(new Mail.HtmlTemplate("ActualizacionPerfilTemplate", properties))
                .subject("Actualización de datos")
                .build();
    }
}
