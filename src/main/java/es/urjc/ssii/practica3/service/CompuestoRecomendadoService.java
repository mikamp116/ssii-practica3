package es.urjc.ssii.practica3.service;

import es.urjc.ssii.practica3.entity.opcionA.CompuestoRecomendadoH1;
import es.urjc.ssii.practica3.entity.opcionA.CompuestoRecomendadoH2;
import es.urjc.ssii.practica3.entity.opcionA.CompuestoRecomendadoH3;
import es.urjc.ssii.practica3.entity.opcionA.CompuestoRecomendadoH4;
import es.urjc.ssii.practica3.entity.opcionB.CompuestoRecomendado;
import es.urjc.ssii.practica3.repository.opcionA.CompuestoRecomendadoH1Repository;
import es.urjc.ssii.practica3.repository.opcionA.CompuestoRecomendadoH2Repository;
import es.urjc.ssii.practica3.repository.opcionA.CompuestoRecomendadoH3Repository;
import es.urjc.ssii.practica3.repository.opcionA.CompuestoRecomendadoH4Repository;
import es.urjc.ssii.practica3.repository.opcionB.CompuestoRecomendadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author Victor Fernandez Fernandez, Mikayel Mardanyan Petrosyan
 */
@Service
public class CompuestoRecomendadoService {

    /* Opcion A */
    @Autowired
    private CompuestoRecomendadoH1Repository repositorio1;
    @Autowired
    private CompuestoRecomendadoH2Repository repositorio2;
    @Autowired
    private CompuestoRecomendadoH3Repository repositorio3;
    @Autowired
    private CompuestoRecomendadoH4Repository repositorio4;

    /* Opcion B */
    @Autowired
    private CompuestoRecomendadoRepository repositorio;

    public void saveHospital1(CompuestoRecomendadoH1 cr) {
        repositorio1.save(cr);
    }

    public void saveHospital2(CompuestoRecomendadoH2 cr) {
        repositorio2.save(cr);
    }

    public void saveHospital3(CompuestoRecomendadoH3 cr) {
        repositorio3.save(cr);
    }

    public void saveHospital4(CompuestoRecomendadoH4 cr) {
        repositorio4.save(cr);
    }

    public void save(CompuestoRecomendado cr) {
        repositorio.save(cr);
    }
}
