package com.example.proydbp.events.email_event;

import com.example.proydbp.events.model.Mail;
import com.example.proydbp.repartidor.domain.Repartidor;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class BienvenidaRepartidorEvent extends ApplicationEvent {

    final private Mail mail;
    final private Repartidor repartidor;

    public BienvenidaRepartidorEvent(Repartidor repartidor, String recipientEmail){
        super(repartidor);
        this.repartidor = repartidor;

        Map<String, Object> properties = new HashMap<>();
        properties.put("Nombre", repartidor.getFirstName() + " " + repartidor.getLastName());
        properties.put("Email", repartidor.getEmail());
        properties.put("Mensaje", "Gracias por registrarte en nuestra plataforma.");

        this.mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(recipientEmail)
                .htmlTemplate(new Mail.HtmlTemplate("BienvenidaTemplateEmpleado", properties))
                .subject("Bienvenido a nuestro equipo")
                .build();
    }
}
