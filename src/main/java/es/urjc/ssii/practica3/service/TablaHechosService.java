package es.urjc.ssii.practica3.service;

import es.urjc.ssii.practica3.entity.TablaHechos;
import es.urjc.ssii.practica3.repository.TablaHechosRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Mikayel Mardanyan Petrosyan
 */
@Service
public class TablaHechosService {

    private final TablaHechosRepository repositorio;

    public TablaHechosService(TablaHechosRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void save(TablaHechos hechos) {
        repositorio.save(hechos);
    }

    public List<TablaHechos> getAll() {
        return repositorio.findAll();
    }

    public List<TablaHechos> getFallecidos(int tratamiento) {
        return repositorio.findByTratamientoAndFallecido(tratamiento, true);
    }

    public List<TablaHechos> getNoFallecidos(int tratamiento) {
        return repositorio.findByTratamientoAndFallecido(tratamiento, false);
    }

    public int getNumFallecidos(int tratamiento) {
        return repositorio.countByTratamientoAndFallecido(tratamiento, true);
    }

    public int getNumNoFallecidos(int tratamiento) {
        return repositorio.countByTratamientoAndFallecido(tratamiento, false);
    }
}
