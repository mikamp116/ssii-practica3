package es.urjc.ssii.practica3.service;

import es.urjc.ssii.practica3.entity.CompuestoRecomendado;
import es.urjc.ssii.practica3.repository.CompuestoRecomendadoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Mikayel Mardanyan Petrosyan
 */
@Service
public class CompuestoRecomendadoService {

    private final CompuestoRecomendadoRepository repositorio;

    public CompuestoRecomendadoService(CompuestoRecomendadoRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void save(CompuestoRecomendado cr) {
        repositorio.save(cr);
    }

    public List<CompuestoRecomendado> getAll() {
        return repositorio.findAll();
    }
}
