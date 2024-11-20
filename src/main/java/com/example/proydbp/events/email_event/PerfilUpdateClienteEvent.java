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

    public PerfilUpdateClienteEvent(Client client, Map<String, String> updatedFields, String recipientEmail){
        super(client);
        this.client = client;

        // Propiedades del correo basadas en los campos actualizados
        Map<String, Object> properties = new HashMap<>();
        properties.put("Nombre", client.getFirstName() + " " + client.getLastName());
        properties.put("FirstName", client.getFirstName());
        properties.put("updatedFields", updatedFields); // Campos modificados como lista
        properties.put("updatedAt", client.getUpdatedAt().toString()); // Fecha de actualización

        this.mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(recipientEmail)
                .htmlTemplate(new Mail.HtmlTemplate("ActualizacionPerfilTemplate", properties))
                .subject("Actualización de datos")
                .build();
    }
}
