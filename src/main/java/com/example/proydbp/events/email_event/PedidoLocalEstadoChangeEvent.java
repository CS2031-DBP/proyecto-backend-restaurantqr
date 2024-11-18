package com.example.proydbp.events.email_event;

import com.example.proydbp.pedido_local.domain.PedidoLocal;
import com.example.proydbp.pedido_local.domain.StatusPedidoLocal;
import com.example.proydbp.events.model.Mail;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class PedidoLocalEstadoChangeEvent extends ApplicationEvent {

    private final Mail mail;
    private final PedidoLocal pedidoLocal;

    public PedidoLocalEstadoChangeEvent(PedidoLocal pedidoLocal, String recipientEmail) {
        super(pedidoLocal);
        this.pedidoLocal = pedidoLocal;

        Map<String, Object> properties = new HashMap<>();
        properties.put("id", pedidoLocal.getId());
        properties.put("Estado", pedidoLocal.getStatus().name());
        properties.put("imageUrl", getImageUrlForStatus(pedidoLocal.getStatus())); // Pasar el StatusPedidoLocal para la imagen

        this.mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(recipientEmail)
                .htmlTemplate(new Mail.HtmlTemplate("PedidoLocalEstadoChangeTemplate", properties))
                .subject("Estado de su Pedido Local actualizado")
                .build();
    }

    private String getImageUrlForStatus(StatusPedidoLocal status) {
        switch (status) {
            case EN_PREPARACION:
                return "https://img.freepik.com/foto-gratis/cocinero-preparando-pizza-italiana_1150-9069.jpg"; // Imagen para "En Preparación"
            case LISTO:
                return "https://png.pngtree.com/png-vector/20240129/ourlarge/pngtree-takeaway-food-png-ai-generative-png-image_11562915.png"; // Imagen para "Listo"
            case ENTREGADO:
                return "https://cdn-icons-png.flaticon.com/512/2558/2558190.png"; // Imagen para "Entregado"
            case CANCELADO:
                return "https://cdn-icons-png.flaticon.com/512/1147/1147931.png"; // Imagen para "Cancelado"
            default:
                return "https://www.pngkey.com/png/detail/328-3288906_chef-chino-png.png"; // Imagen por defecto
        }
    }
}
