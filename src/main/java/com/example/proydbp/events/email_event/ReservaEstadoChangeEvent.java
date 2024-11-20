package com.example.proydbp.events.email_event;

import com.example.proydbp.events.model.Mail;
import com.example.proydbp.reservation.domain.Reservation;
import com.example.proydbp.reservation.domain.StatusReservation;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ReservaEstadoChangeEvent extends ApplicationEvent {

    final private Mail mail;
    final private Reservation reservation;

    public ReservaEstadoChangeEvent(Reservation reservation, String recipientEmail) {
        super(reservation);
        this.reservation = reservation;

        if (reservation.getStatusReservation() == null) {
            reservation.setStatusReservation(StatusReservation.PENDIENTE); // Establecer PENDIENTE por defecto
        }

        Map<String, Object> properties = new HashMap<>();
        properties.put("id", reservation.getId());
        properties.put("Estado", reservation.getStatusReservation().name()); // Usamos el nombre del estado
        properties.put("imageUrl", getImageUrlForStatus(reservation.getStatusReservation())); // Determinamos la imagen para el estado de la reserva

        this.mail = Mail.builder()
                .from("fernando.munoz.p@utec.edu.pe")
                .to(recipientEmail)
                .htmlTemplate(new Mail.HtmlTemplate("ReservaEstadoChangeTemplate", properties))
                .subject("Actualización del estado de su reserva")
                .build();
    }

    private String getImageUrlForStatus(StatusReservation status) {
        switch (status) {
            case CONFIRMADO:
                return "https://i2.wp.com/reikimaria.com/wordpress/wordpress/wp-content/uploads/2017/05/RESERVA-COK.png?fit=242%2C220&ssl=1"; // Imagen para "Confirmado"
            case CANCELADO:
                return "https://cdn-icons-png.flaticon.com/512/1147/1147931.png"; // Imagen para "Cancelado"
            case FINALIZADA:
                return "https://www.erotisados.com/gracias.png"; // Imagen para "Finalizada"
            default:
                return "https://cdn-icons-png.flaticon.com/512/5978/5978235.png"; // Imagen por defecto
        }
    }
}
