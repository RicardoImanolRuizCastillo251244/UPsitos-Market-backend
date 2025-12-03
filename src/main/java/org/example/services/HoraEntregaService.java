package org.example.services;

import org.example.models.HoraEntrega;
import org.example.repositories.HoraEntregaRepository;

import java.sql.SQLException;
import java.util.List;

public class HoraEntregaService {

    private final HoraEntregaRepository repo;

    public HoraEntregaService(HoraEntregaRepository repo) {
        this.repo = repo;
    }

    public void delete(int id_horario) throws Exception {
        repo.delete(id_horario);
    }

    public void saveMultiple(int idUsuario, int[] horarios) throws Exception {
        for (int idHorario : horarios) {
            repo.save(idUsuario, idHorario);
        }
    }

    public List<HoraEntrega> findAll() throws Exception {
        return repo.findAll();
    }

    public List<HoraEntrega> findById(int idUsuario) throws Exception {
        return repo.findById(idUsuario);
    }
}
