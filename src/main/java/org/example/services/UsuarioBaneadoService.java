package org.example.services;

import org.example.models.Usuario;
import org.example.models.UsuarioBaneado;
import org.example.repositories.UsuarioBaneadoRepository;
import org.example.repositories.UsuarioRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public class UsuarioBaneadoService {
    private final UsuarioBaneadoRepository ubRepository;
    private final UsuarioRepository usuarioRepository;

    public UsuarioBaneadoService(UsuarioBaneadoRepository ubRepository, UsuarioRepository usuarioRepository) {
        this.ubRepository = ubRepository;
        this.usuarioRepository = usuarioRepository;
    }

    private void validateUsuarioBaneado(UsuarioBaneado ub) {
        if (ub.getId_usuario() <= 0) {
            throw new IllegalArgumentException("El ID de usuario no es válido.");
        }
        if (ub.getMotivo() == null || ub.getMotivo().trim().isEmpty()) {
            throw new IllegalArgumentException("El motivo del baneo no puede estar vacío.");
        }
    }

    public UsuarioBaneado banearUsuario(UsuarioBaneado ub) throws Exception {
        validateUsuarioBaneado(ub);

        Usuario usuario = usuarioRepository.findById(ub.getId_usuario())
                .orElseThrow(() -> new IllegalArgumentException("El usuario con ID " + ub.getId_usuario() + " no existe."));

        if (ub.getFecha() == null) {
            ub.setFecha(LocalDateTime.now());
        }

        UsuarioBaneado baneado = ubRepository.save(ub);
        usuario.setActivo(false);
        usuarioRepository.update(usuario);

        return baneado;
    }

    public void desbanearUsuario(int id_usuario) throws Exception {
        if (id_usuario <= 0) throw new IllegalArgumentException("El ID de usuario no es válido.");

        Usuario usuario = usuarioRepository.findById(id_usuario)
                .orElseThrow(() -> new IllegalArgumentException("El usuario con ID " + id_usuario + " no existe."));

        ubRepository.deleteById(id_usuario);
        usuario.setActivo(true);
        usuarioRepository.update(usuario);
    }

    public Optional<UsuarioBaneado> findById(int id_usuario) throws Exception {
        if (id_usuario <= 0) throw new IllegalArgumentException("El ID de usuario no es válido.");
        return ubRepository.findById(id_usuario);
    }

    public List<UsuarioBaneado> findAll() throws Exception {
        return ubRepository.findAll();
    }
}