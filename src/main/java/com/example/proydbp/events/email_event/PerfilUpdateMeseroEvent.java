package com.example.proydbp.events.email_event;

import com.example.proydbp.events.model.Mail;
import com.example.proydbp.mesero.domain.Mesero;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PerfilUpdateMeseroEvent extends ApplicationEvent {

    private final Mail mail;
    private final Mesero mesero;

    public PerfilUpdateMeseroEvent(Mesero mesero, Map<String, String> updatedFields, String recipientEmail) {
        super(mesero);
        this.mesero = mesero;

        // Propiedades del correo basadas en los campos actualizados
        Map<String, Object> properties = new HashMap<>();
        properties.put("Nombre", mesero.getFirstName() + " " + mesero.getLastName());
        properties.put("updatedFields", updatedFields); // Campos modificados como lista
        properties.put("updatedAt", mesero.getUpdatedAt().toString()); // Fecha de actualización

        // Crear el correo
        this.mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(recipientEmail)
                .htmlTemplate(new Mail.HtmlTemplate("ActualizacionPerfilTemplate", properties))
                .subject("Actualización de datos")
                .build();
    }
}
