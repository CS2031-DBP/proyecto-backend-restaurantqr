package com.example.proydbp.events.email_event;

import com.example.proydbp.events.model.Mail;
import com.example.proydbp.pedido_local.domain.PedidoLocal;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PedidoLocalCrearMeseroEvent extends ApplicationEvent {

    final private Mail mail;
    final private PedidoLocal pedidoLocal;

    public PedidoLocalCrearMeseroEvent(PedidoLocal pedidoLocal, String meseroEmail) {
        super(pedidoLocal);
        this.pedidoLocal = pedidoLocal;

        String mesaId = pedidoLocal.getMesa() != null ? pedidoLocal.getMesa().getId().toString() : "Mesa no asignada";
        String clienteNombre = pedidoLocal.getClient() != null ? pedidoLocal.getClient().getFirstName() : "Cliente no disponible";
        String fechaOrden = pedidoLocal.getFecha().toLocalDate().toString();
        String horaOrden = pedidoLocal.getFecha().toLocalTime().toString();

        Map<String, Object> properties = new HashMap<>();
        properties.put("id", pedidoLocal.getId());
        properties.put("MesaId", mesaId);
        properties.put("Nombre del Cliente", clienteNombre);
        properties.put("Fecha de Orden", fechaOrden);
        properties.put("Hora de Orden", horaOrden);

        this.mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(meseroEmail)
                .htmlTemplate(new Mail.HtmlTemplate("PedidoLocalMeseroAsignadoTemplate", properties))
                .subject("Nuevo Pedido Asignado")
                .build();
    }
}
