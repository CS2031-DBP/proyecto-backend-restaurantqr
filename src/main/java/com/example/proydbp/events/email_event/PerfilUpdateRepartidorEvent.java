package com.example.proydbp.events.email_event;

import com.example.proydbp.events.model.Mail;
import com.example.proydbp.repartidor.domain.Repartidor;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PerfilUpdateRepartidorEvent extends ApplicationEvent {

    final private Mail mail;
    final private Repartidor repartidor;

    public PerfilUpdateRepartidorEvent(Repartidor repartidor, String recipientEmail){
        super(repartidor);
        this.repartidor = repartidor;

        Map<String, Object> properties = new HashMap<>();
        properties.put("firstName", repartidor.getFirstName());
        properties.put("lastName", repartidor.getLastName());
        properties.put("phone", repartidor.getPhoneNumber());
        properties.put("updatedAt", repartidor.getUpdatedAt().toString()); // fecha de actualización

        this.mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(recipientEmail)
                .htmlTemplate(new Mail.HtmlTemplate("ActualizacionPerfilTemplate", properties))
                .subject("Actualización de datos")
                .build();
    }
}
