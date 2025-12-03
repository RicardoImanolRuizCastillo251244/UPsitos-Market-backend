package org.example.services;

import org.example.models.Publicacion;
import org.example.models.UsuarioMembresia;
import org.example.repositories.PublicacionRepository;
import org.example.repositories.UsuarioMembresiaRepository; // 1. IMPORTANTE: Importar esto

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PublicacionService {

    private final PublicacionRepository publicacionRepository;

    // 2. IMPORTANTE: Declaramos la variable que te faltaba
    private final UsuarioMembresiaRepository umRepository;

    // 3. IMPORTANTE: Actualizamos el constructor para recibir AMBOS repositorios
    public PublicacionService(PublicacionRepository publicacionRepository, UsuarioMembresiaRepository umRepository) {
        this.publicacionRepository = publicacionRepository;
        this.umRepository = umRepository; // Asignamos la variable
    }

    // --- LÓGICA DE NEGOCIO PRINCIPAL (Validación de Membresía) ---
    public Publicacion savePublicacion(Publicacion publicacion) throws Exception {

        // A. Usamos la variable umRepository para buscar la membresía del vendedor
        Optional<UsuarioMembresia> membresiaOpt = umRepository.findActiveByUserId(publicacion.getId_vendedor());

        if (membresiaOpt.isEmpty()) {
            throw new IllegalArgumentException("El usuario no tiene una membresía activa y no puede publicar.");
        }

        UsuarioMembresia membresia = membresiaOpt.get();
        int tipoMembresia = membresia.getId_membresia_tipo();

        // B. Validar límites según el tipo de membresía
        if (tipoMembresia == 1) {
            // ID 1: Membresía Básica (Solo 3 publicaciones)
            int publicacionesActivas = publicacionRepository.countActiveByVendedor(publicacion.getId_vendedor());
            if (publicacionesActivas >= 3) {
                throw new IllegalStateException("Tu membresía Básica solo te permite tener 3 publicaciones activas. Actualiza a Premium para publicar más.");
            }
        }
        // ID 2: Membresía Premium (Ilimitada) -> Pasa directo sin validación de cantidad

        // C. Si cumple con las reglas, guardamos la publicación
        return publicacionRepository.save(publicacion);
    }

    // --- MÉTODOS EXISTENTES (Se mantienen igual) ---

    public List<Publicacion> getPublicacionesOrderByPrecioAsc() throws Exception {
        List<Publicacion> publicaciones = publicacionRepository.findAllOrderByPrecioAsc();
        if (publicaciones.isEmpty()) {
            throw new Exception("No se encontraron publicaciones ordenadas por precio ascendente");
        }
        return publicaciones;
    }

    public List<Publicacion> getPublicacionesOrderByPrecioDesc() throws Exception {
        List<Publicacion> publicaciones = publicacionRepository.findAllOrderByPrecioDesc();
        if (publicaciones.isEmpty()) {
            throw new Exception("No se encontraron publicaciones ordenadas por precio descendente");
        }
        return publicaciones;
    }

    public List<Publicacion> getAllPublicaciones() throws SQLException {
        return publicacionRepository.findAll();
    }

    public Optional<Publicacion> getPublicacionById(int id) throws SQLException {
        return publicacionRepository.findById(id);
    }

    public void updatePublicacion(Publicacion publicacion) throws SQLException {
        publicacionRepository.update(publicacion);
    }

    public void deletePublicacion(int id) throws SQLException {
        publicacionRepository.deleteById(id);
    }

    public List<Publicacion> getPublicacionesByCategoria(int idCategoria) throws SQLException {
        return publicacionRepository.findByCategoria(idCategoria);
    }

    public List<Publicacion> getPublicacionesByTitulo(String titulo) throws SQLException {
        if (titulo == null || titulo.trim().isEmpty()) {
            return List.of();
        }
        return publicacionRepository.findByTitulo(titulo);
    }

    public List<Publicacion> getPremiumSellerPublications() throws SQLException {
        return publicacionRepository.findPremiumSellerPublications();
    }
}