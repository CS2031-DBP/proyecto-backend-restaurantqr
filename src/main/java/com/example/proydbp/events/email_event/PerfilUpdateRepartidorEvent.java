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

    public PerfilUpdateRepartidorEvent(Repartidor repartidor, Map<String, String> updatedFields, String recipientEmail){
        super(repartidor);
        this.repartidor = repartidor;

        // Propiedades del correo basadas en los campos actualizados
        Map<String, Object> properties = new HashMap<>();
        properties.put("Nombre", repartidor.getFirstName() + " " + repartidor.getLastName());
        properties.put("updatedFields", updatedFields); // Campos modificados como lista
        properties.put("updatedAt", repartidor.getUpdatedAt().toString()); // Fecha de actualización

        this.mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(recipientEmail)
                .htmlTemplate(new Mail.HtmlTemplate("ActualizacionPerfilTemplate", properties))
                .subject("Actualización de datos")
                .build();
    }
}
