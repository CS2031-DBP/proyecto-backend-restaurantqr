package com.example.proydbp.events.email_event;

import com.example.proydbp.delivery.domain.Delivery;
import com.example.proydbp.delivery.domain.StatusDelivery;
import com.example.proydbp.events.model.Mail;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class DeliveryEstadoChangeEvent extends ApplicationEvent {

    private final Mail mail;
    private final Delivery delivery;

    public DeliveryEstadoChangeEvent(Delivery delivery, String recipientEmail) {
        super(delivery);
        this.delivery = delivery;

        Map<String, Object> properties = new HashMap<>();
        properties.put("id", delivery.getId());
        properties.put("Estado", delivery.getStatus().name());
        properties.put("imageUrl", getImageUrlForStatus(delivery.getStatus())); // Pasar el StatusDelivery para la imagen

        this.mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(recipientEmail)
                .htmlTemplate(new Mail.HtmlTemplate("DeliveryEstadoChangeTemplate", properties))
                .subject("Estado de su orden de delivery actualizado")
                .build();
    }

    private String getImageUrlForStatus(StatusDelivery status) {
        switch (status) {
            case EN_PREPARACION:
                return "https://img.freepik.com/fotos-premium/lugar-trabajo-chef-profesional-cocina-restaurante-vista-cerca-hombre-moviendo-sopa-mano_763111-6779.jpg";
            case LISTO:
                return "https://png.pngtree.com/png-vector/20240129/ourlarge/pngtree-takeaway-food-png-ai-generative-png-image_11562915.png";
            case EN_CAMINO:
                return "https://i.pinimg.com/736x/d1/d6/e1/d1d6e1aabd1f091c0e55f84a9fd78f19.jpg";
            case ENTREGADO:
                return "https://cdn-icons-png.flaticon.com/512/2558/2558190.png";
            case CANCELADO:
                return "https://cdn-icons-png.flaticon.com/512/1147/1147931.png";
            default:
                return "https://www.pngkey.com/png/detail/328-3288906_chef-chino-png.png"; // Imagen por defecto
        }
    }
}
