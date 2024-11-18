package com.example.proydbp.events.email_event;

import com.example.proydbp.client.domain.Client;
import com.example.proydbp.events.model.Mail;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class BienvenidaClienteEvent extends ApplicationEvent {

    private final Mail mail;
    private final Client client;

    public BienvenidaClienteEvent(Client client, String recipientEmail){
        super(client);
        this.client = client;

        Map<String, Object> properties = new HashMap<>();
        properties.put("Nombre", client.getFirstName() + " " + client.getLastName());
        properties.put("Email", client.getEmail());
        properties.put("Mensaje", "Gracias por registrarte en nuestra plataforma.");

        this.mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(recipientEmail)
                .htmlTemplate(new Mail.HtmlTemplate("BienvenidaTemplate", properties))
                .subject("¡Bienvenido a nuestra plataforma!")
                .build();
    }
}
