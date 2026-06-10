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
    public Optional<Buzon> buscarBuzon(Integer id) {
        return buzonRepository.findById(id);
    }

    public Buzon actualizarBuzon(Integer id, Buzon datos) {
        Buzon buzon = buzonRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Buzon no encontrado con ID: " + id));

        buzon.setUsuarioId(datos.getUsuarioId());
        buzon.setNombreJugador(datos.getNombreJugador());
        return buzonRepository.save(buzon);
    }

    public void eliminarBuzon(Integer id) {
        if (!buzonRepository.existsById(id)) {
            throw new RuntimeException("Buzon no encontrado con ID: " + id);
        }

        buzonRepository.deleteById(id);
    }

    public Optional<Notificacion> buscarNotificacion(Integer id) {
        return notificacionRepository.findById(id);
    }

    public Notificacion actualizarNotificacion(Integer id, Notificacion datos) {
        Notificacion notificacion = notificacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificacion no encontrada"));

        notificacion.setTitulo(datos.getTitulo());
        notificacion.setMensaje(datos.getMensaje());
        notificacion.setLeida(datos.getLeida());

        if (datos.getBuzon() != null && datos.getBuzon().getId() != null) {
            Buzon buzon = buzonRepository.findById(datos.getBuzon().getId())
                    .orElseThrow(() -> new RuntimeException("Buzon no encontrado con ID: " + datos.getBuzon().getId()));
            notificacion.setBuzon(buzon);
        }

        return notificacionRepository.save(notificacion);
    }

    public void eliminarNotificacion(Integer id) {
        if (!notificacionRepository.existsById(id)) {
            throw new RuntimeException("Notificacion no encontrada");
        }

        notificacionRepository.deleteById(id);
    }
}
