package com.DragonForge.notification_service.service;

import com.DragonForge.notification_service.client.UserClient;
import com.DragonForge.notification_service.dto.UsuarioDTO;
import com.DragonForge.notification_service.model.Buzon;
import com.DragonForge.notification_service.model.Notificacion;
import com.DragonForge.notification_service.repository.BuzonRepository;
import com.DragonForge.notification_service.repository.NotificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private BuzonRepository buzonRepository;

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private UserClient userClient;

    public List<Buzon> listarBuzones() {
        return buzonRepository.findAll();
    }

    public Optional<Buzon> buscarBuzonPorUsuarioId(Integer usuarioId) {
        return buzonRepository.findByUsuarioId(usuarioId);
    }

    public Buzon crearBuzon(Buzon buzon) {

        UsuarioDTO usuario = userClient.obtenerUsuarioPorId(buzon.getUsuarioId());

        buzon.setNombreJugador(usuario.getUsername());
        return buzonRepository.save(buzon);
    }

    public List<Notificacion> listarNotificaciones(Integer buzonId) {
        return notificacionRepository.findByBuzonId(buzonId);
    }

    public List<Notificacion> listarNotificacionesNoLeidas(Integer buzonId) {
        return notificacionRepository.findByBuzonIdAndLeidaFalse(buzonId);
    }

    public Notificacion enviarNotificacion(Integer buzonId, Notificacion notificacion) {
        Buzon buzon = buzonRepository.findById(buzonId)
                .orElseThrow(() -> new RuntimeException("Buzón no encontrado con ID: " + buzonId));

        notificacion.setBuzon(buzon);
        return notificacionRepository.save(notificacion);
    }

    public Notificacion marcarComoLeida(Integer notificacionId) {
        Notificacion notificacion = notificacionRepository.findById(notificacionId)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));

        notificacion.setLeida(true);
        return notificacionRepository.save(notificacion);
    }
}