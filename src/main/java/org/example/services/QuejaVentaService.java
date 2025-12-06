package org.example.services;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.example.models.Notificacion;
import org.example.models.Publicacion;
import org.example.models.QuejaVenta;
import org.example.models.QuejaVentaDTO;
import org.example.models.Usuario;
import org.example.models.Venta;
import org.example.repositories.NotificacionRepository;
import org.example.repositories.PublicacionRepository;
import org.example.repositories.QuejaVentaRepository;
import org.example.repositories.UsuarioRepository;
import org.example.repositories.VentaRepository;

public class QuejaVentaService {
    private final QuejaVentaRepository quejaRepository;
    private final VentaRepository ventaRepository;
    private final PublicacionRepository publicacionRepository;
    private final UsuarioRepository usuarioRepository;
    private final NotificacionRepository notificacionRepository;

    public QuejaVentaService(QuejaVentaRepository quejaRepository, VentaRepository ventaRepository, PublicacionRepository publicacionRepository, UsuarioRepository usuarioRepository, NotificacionRepository notificacionRepository) {
        this.quejaRepository = quejaRepository;
        this.ventaRepository = ventaRepository;
        this.publicacionRepository = publicacionRepository;
        this.usuarioRepository = usuarioRepository;
        this.notificacionRepository = notificacionRepository;
    }

    private void validateQueja(QuejaVenta queja) throws Exception {
        if (queja.getId_venta() <= 0 || queja.getId_emisor() <= 0) {
            throw new IllegalArgumentException("Los IDs de venta y emisor no son válidos.");
        }
        if (queja.getDescripcion_queja() == null || queja.getDescripcion_queja().trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción de la queja no puede estar vacía.");
        }

        Venta venta = ventaRepository.findById(queja.getId_venta())
                .orElseThrow(() -> new IllegalArgumentException("La venta con ID " + queja.getId_venta() + " no existe."));

        Publicacion publicacion = publicacionRepository.findById(venta.getId_publicacion())
                .orElseThrow(() -> new IllegalStateException("La publicación asociada a la venta no existe."));

        if (queja.getId_emisor() != venta.getId_comprador() && queja.getId_emisor() != publicacion.getId_vendedor()) {
            throw new IllegalArgumentException("Solo el comprador o el vendedor pueden registrar una queja sobre esta venta.");
        }
    }

    public QuejaVenta save(QuejaVenta queja) throws Exception {
        validateQueja(queja);
        if (queja.getEstado_queja() == null) {
            queja.setEstado_queja("ABIERTA");
        }
        try {
            QuejaVenta quejaGuardada = quejaRepository.save(queja);
            
            // Notificar a todos los administradores
            List<Usuario> admins = usuarioRepository.findAdmins();
            String mensaje = "Nueva queja recibida sobre Venta. Revisar inmediatamente.";
            
            for (Usuario admin : admins) {
                Notificacion notificacion = new Notificacion();
                notificacion.setId_usuario(admin.getId_usuario());
                notificacion.setMensaje(mensaje);
                notificacion.setTipo("QUEJA_VENTA");
                notificacion.setLeida(false);
                notificacion.setFecha_envio(LocalDateTime.now());
                notificacionRepository.save(notificacion);
            }
            
            return quejaGuardada;
        } catch (SQLException e) {
            throw new Exception("Error al guardar la queja de venta: " + e.getMessage(), e);
        }
    }

    public void update(QuejaVenta queja) throws Exception {
        if (queja.getId() <= 0) {
            throw new IllegalArgumentException("El ID de la queja no es válido.");
        }
        quejaRepository.update(queja);
    }

    public void deleteById(int id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("El ID no es válido.");
        quejaRepository.deleteById(id);
    }

    public Optional<QuejaVenta> findById(int id) throws Exception {
        if (id <= 0) throw new IllegalArgumentException("El ID no es válido.");
        return quejaRepository.findById(id);
    }

    public List<QuejaVenta> findAll() throws Exception {
        return quejaRepository.findAll();
    }

    public List<QuejaVentaDTO> findAllWithDetails() throws Exception {
        try {
            return quejaRepository.findAllWithDetails();
        } catch (SQLException e) {
            throw new Exception("Error al obtener las quejas de venta con detalles: " + e.getMessage(), e);
        }
    }
}