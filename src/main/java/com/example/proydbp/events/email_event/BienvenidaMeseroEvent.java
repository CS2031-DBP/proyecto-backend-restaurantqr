package com.example.proydbp.events.email_event;

import com.example.proydbp.events.model.Mail;
import com.example.proydbp.mesero.domain.Mesero;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class BienvenidaMeseroEvent extends ApplicationEvent {

    private final Mail mail;
    private final Mesero mesero;

    public BienvenidaMeseroEvent(Mesero mesero, String recipientEmail){
        super(mesero);
        this.mesero = mesero;

        Map<String, Object> properties = new HashMap<>();
        properties.put("Nombre", mesero.getFirstName() + " " + mesero.getLastName());
        properties.put("Email", mesero.getEmail());
        properties.put("Mensaje", "Gracias por registrarte en nuestra plataforma.");

        this.mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(recipientEmail)
                .htmlTemplate(new Mail.HtmlTemplate("BienvenidaTemplateEmpleado", properties))
                .subject("Bienvenido a nuestro equipo")
                .build();
    }
}
