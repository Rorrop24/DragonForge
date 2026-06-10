package com.DragonForge.user_service.service;

import com.DragonForge.user_service.model.Campana;
import com.DragonForge.user_service.model.Usuario;
import com.DragonForge.user_service.repository.CampanaRepository;
import com.DragonForge.user_service.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CampanaRepository campanaRepository;

    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> buscarUsuario(Integer id) {
        return usuarioRepository.findById(id);
    }

    public Usuario registrarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    public List<Campana> verCampanasDeUsuario(Integer usuarioId) {
        return campanaRepository.findByDungeonMasterId(usuarioId);
    }

    public Campana crearCampana(Integer usuarioId, Campana campana) {
        Usuario dm = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        campana.setDungeonMaster(dm);
        return campanaRepository.save(campana);
    }
    public Usuario actualizarUsuario(Integer id, Usuario datos) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setUsername(datos.getUsername());
        usuario.setEmail(datos.getEmail());
        usuario.setPassword(datos.getPassword());
        return usuarioRepository.save(usuario);
    }

    public void eliminarUsuario(Integer id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }

        usuarioRepository.deleteById(id);
    }

    public Optional<Campana> buscarCampana(Integer id) {
        return campanaRepository.findById(id);
    }

    public Campana actualizarCampana(Integer id, Campana datos) {
        Campana campana = campanaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Campana no encontrada"));

        campana.setNombre(datos.getNombre());
        campana.setDescripcion(datos.getDescripcion());

        if (datos.getDungeonMaster() != null && datos.getDungeonMaster().getId() != null) {
            Usuario dm = usuarioRepository.findById(datos.getDungeonMaster().getId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            campana.setDungeonMaster(dm);
        }

        return campanaRepository.save(campana);
    }

    public void eliminarCampana(Integer id) {
        if (!campanaRepository.existsById(id)) {
            throw new RuntimeException("Campana no encontrada");
        }

        campanaRepository.deleteById(id);
    }
}
