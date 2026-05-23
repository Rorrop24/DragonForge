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
}