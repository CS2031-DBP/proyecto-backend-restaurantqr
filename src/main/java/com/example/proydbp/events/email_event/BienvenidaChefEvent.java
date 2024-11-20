package com.example.proydbp.events.email_event;

import com.example.proydbp.events.model.Mail;
import com.example.proydbp.user.domain.User;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class BienvenidaChefEvent extends ApplicationEvent {

    final private Mail mail;
    final private User user;

    public BienvenidaChefEvent(User chef, String recipientEmail){
        super(chef);
        this.user = chef;

        Map<String, Object> properties = new HashMap<>();
        properties.put("Nombre", chef.getFirstName() + " " + chef.getLastName());
        properties.put("Email", chef.getEmail());
        properties.put("Mensaje", "Gracias por registrarte en nuestra plataforma.");

        this.mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(recipientEmail)
                .htmlTemplate(new Mail.HtmlTemplate("BienvenidaTemplateEmpleado", properties))
                .subject("Bienvenido a nuestro equipo")
                .build();
    }
}
