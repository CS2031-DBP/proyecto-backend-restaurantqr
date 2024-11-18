package com.example.proydbp.events.email_event;

import com.example.proydbp.client.domain.Client;
import com.example.proydbp.events.model.Mail;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PerfilUpdateClienteEvent extends ApplicationEvent {

    final private Mail mail;
    final private Client client;

    public PerfilUpdateClienteEvent(Client client, String recipientEmail){
        super(client);
        this.client = client;

        Map<String, Object> properties = new HashMap<>();
        properties.put("firstName", client.getFirstName());
        properties.put("lastName", client.getLastName());
        properties.put("phone", client.getPhoneNumber());
        properties.put("updatedAt", client.getUpdatedAt().toString()); // fecha de actualización


        this.mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(recipientEmail)
                .htmlTemplate(new Mail.HtmlTemplate("ActualizacionPerfilTemplate", properties))
                .subject("Actualización de datos")
                .build();
    }
}
