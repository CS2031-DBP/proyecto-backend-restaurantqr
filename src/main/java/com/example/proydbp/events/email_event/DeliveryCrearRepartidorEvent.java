package com.example.proydbp.events.email_event;

import com.example.proydbp.delivery.domain.Delivery;
import com.example.proydbp.events.model.Mail;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class DeliveryCrearRepartidorEvent extends ApplicationEvent {

    final private Mail mail;
    final private Delivery delivery;

    public DeliveryCrearRepartidorEvent(Delivery delivery, String repartidorEmail) {
        super(delivery);
        this.delivery = delivery;

        // Obtener el cliente asociado a la entrega
        String clienteNombre = delivery.getClient() != null ? delivery.getClient().getFirstName() : "Cliente no disponible";
        String clienteTelefono = delivery.getClient() != null ? delivery.getClient().getPhoneNumber() : "No disponible";
        String direccionCliente = delivery.getDireccion();
        String fechaOrden = delivery.getFecha().toLocalDate().toString(); // Solo la fecha (ejemplo: 2024-11-17)
        String horaOrden = delivery.getFecha().toLocalTime().toString(); // Solo la hora (ejemplo: 12:34:56.789)
        String repartidorNombre = delivery.getRepartidor().getFirstName() + " " + delivery.getRepartidor().getLastName();

        Map<String, Object> properties = new HashMap<>();
        properties.put("id", delivery.getId());
        properties.put("DireccionCliente", direccionCliente);
        properties.put("NombreCliente", clienteNombre);
        properties.put("TelefonoCliente", clienteTelefono);
        properties.put("FechaOrden", fechaOrden);
        properties.put("HoraOrden", horaOrden);
        properties.put("Repartidor", repartidorNombre);

        this.mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(repartidorEmail)  // Correo del repartidor
                .htmlTemplate(new Mail.HtmlTemplate("DeliveryRepartidorCreadoTemplate", properties))
                .subject("Se le ha asignado una nueva entrega")
                .build();
    }
}
