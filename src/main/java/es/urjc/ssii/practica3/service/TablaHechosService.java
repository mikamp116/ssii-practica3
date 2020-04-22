package es.urjc.ssii.practica3.service;

import es.urjc.ssii.practica3.entity.TablaHechos;
import es.urjc.ssii.practica3.repository.TablaHechosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Victor Fernandez Fernandez, Mikayel Mardanyan Petrosyan
 */
@Service
public class TablaHechosService {

    @Autowired
    private TablaHechosRepository repositorio;

    public void save(TablaHechos hechos) {
        repositorio.save(hechos);
    }

    public List<TablaHechos> getAll() {
        return repositorio.findAll();
    }
}
