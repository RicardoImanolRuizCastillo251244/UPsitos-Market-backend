package org.example.services;

import org.example.models.Publicacion;
import org.example.models.Usuario;
import org.example.repositories.PublicacionRepository;
import org.example.repositories.UsuarioRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PublicacionService {
    private final PublicacionRepository publicacionRepository;

    public PublicacionService(PublicacionRepository publicacionRepository) {
        this.publicacionRepository = publicacionRepository;
    }

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


    public Publicacion savePublicacion(Publicacion publicacion) throws SQLException {

        return publicacionRepository.save(publicacion);
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