package es.urjc.ssii.practica3.service;

import es.urjc.ssii.practica3.entity.CompuestoRecomendado;
import es.urjc.ssii.practica3.repository.CompuestoRecomendadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Victor Fernandez Fernandez, Mikayel Mardanyan Petrosyan
 */
@Service
public class CompuestoRecomendadoService {

    @Autowired
    private CompuestoRecomendadoRepository repositorio;

    public void save(CompuestoRecomendado cr) {
        repositorio.save(cr);
    }
}
