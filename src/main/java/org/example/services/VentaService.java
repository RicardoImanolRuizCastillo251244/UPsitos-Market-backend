package org.example.services;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.example.models.CompraDTO;
import org.example.models.Notificacion;
import org.example.models.Publicacion;
import org.example.models.Venta;
import org.example.repositories.NotificacionRepository;
import org.example.repositories.PublicacionRepository;
import org.example.repositories.VentaRepository;

public class VentaService {
    private final VentaRepository ventaRepository;
    private final PublicacionRepository publicacionRepository;
    private final NotificacionRepository notificacionRepository;

    public VentaService(VentaRepository ventaRepository, PublicacionRepository publicacionRepository, NotificacionRepository notificacionRepository) {
        this.ventaRepository = ventaRepository;
        this.publicacionRepository = publicacionRepository;
        this.notificacionRepository = notificacionRepository;
    }

    private void validateVenta(Venta venta) {
        if (venta.getId_publicacion() <= 0) {
            throw new IllegalArgumentException("El ID de la publicación no es válido.");
        }
        if (venta.getId_comprador() <= 0) {
            throw new IllegalArgumentException("El ID del comprador no es válido.");
        }
        if (venta.getCantidad_vendida() <= 0) {
            throw new IllegalArgumentException("La cantidad vendida debe ser mayor a cero.");
        }
    }

    public List<Integer> getPublicacionesMasVendidas() throws Exception {
        List<Integer> publicaciones = ventaRepository.findPublicacionesMasVendidas();
        if (publicaciones.isEmpty()) {
            throw new Exception("No se encontraron publicaciones con ventas registradas");
        }
        return publicaciones;
    }


    public Venta save(Venta venta) throws Exception {
        validateVenta(venta);

        Publicacion publicacion = publicacionRepository.findById(venta.getId_publicacion())
                .orElseThrow(() -> new IllegalArgumentException("La publicación con ID " + venta.getId_publicacion() + " no existe."));

        float precioTotal = publicacion.getPrecio_producto() * venta.getCantidad_vendida();
        venta.setPrecio_total(precioTotal);

        if (venta.getFecha_venta() == null) {
            venta.setFecha_venta(LocalDateTime.now());
        }

        try {
            Venta ventaGuardada = ventaRepository.save(venta);
            
            // Notificar al vendedor
            int idVendedor = publicacion.getId_vendedor();
            String mensaje = "¡Felicidades! Has vendido tu producto \"" + publicacion.getTitulo_publicacion() + "\". Revisa los detalles de la venta.";
            Notificacion notificacion = new Notificacion();
            notificacion.setId_usuario(idVendedor);
            notificacion.setMensaje(mensaje);
            notificacion.setTipo("VENTA");
            notificacion.setLeida(false);
            notificacion.setFecha_envio(LocalDateTime.now());
            
            notificacionRepository.save(notificacion);
            
            return ventaGuardada;
        } catch (SQLException e) {
            throw new Exception("Error al guardar la venta: " + e.getMessage(), e);
        }
    }

    public void update(Venta venta) throws Exception {
        if (venta.getId() <= 0) throw new IllegalArgumentException("El ID de la venta no es válido.");
        validateVenta(venta);
        ventaRepository.update(venta);
    }

    public void deleteById(int id) throws Exception {
        ventaRepository.deleteById(id);
    }

    public Optional<Venta> findById(int id) throws Exception {
        return ventaRepository.findById(id);
    }

    public List<Venta> findAll() throws Exception {
        return ventaRepository.findAll();
    }

    public List<CompraDTO> findAllCompras(int id) throws Exception{
        return ventaRepository.findAllCompras(id);
    }

    public List<CompraDTO> findVentasByVendedor(int id) throws Exception {
        return ventaRepository.findVentasByVendedor(id);
    }
}