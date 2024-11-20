package com.example.proydbp.events.email_event;

import com.example.proydbp.events.model.Mail;
import com.example.proydbp.pedido_local.domain.PedidoLocal;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PedidoLocalCrearEvent extends ApplicationEvent {

    final private Mail mail;
    final private PedidoLocal pedidoLocal;

    public PedidoLocalCrearEvent(PedidoLocal pedidoLocal, String clienteEmail) {
        super(pedidoLocal);
        this.pedidoLocal = pedidoLocal;

        String mesaId = pedidoLocal.getMesa() != null ? pedidoLocal.getMesa().getId().toString() : "Mesa no asignada";
        String fechaOrden = pedidoLocal.getFecha().toLocalDate().toString();
        String horaOrden = pedidoLocal.getFecha().toLocalTime().toString();

        Map<String, Object> properties = new HashMap<>();
        properties.put("id", pedidoLocal.getId());
        properties.put("MesaId", mesaId);
        properties.put("FechaOrden", fechaOrden);
        properties.put("HoraOrden", horaOrden);

        this.mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(clienteEmail)
                .htmlTemplate(new Mail.HtmlTemplate("PedidoLocalClienteCreadoTemplate", properties))
                .subject("Confirmación de su pedido en el local")
                .build();
    }
}
