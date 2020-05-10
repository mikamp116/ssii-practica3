package es.urjc.ssii.practica3.repository.opcionA;

import es.urjc.ssii.practica3.entity.opcionA.CompuestoRecomendadoH1;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;


/**
 * @author Victor Fernandez Fernandez, Mikayel Mardanyan Petrosyan
 */

@RepositoryRestResource(collectionResourceRel = "compuesto-recomendado1", path = "compuesto-recomendado1")
public interface CompuestoRecomendadoH1Repository extends CrudRepository<CompuestoRecomendadoH1, Integer> {

    List<CompuestoRecomendadoH1> findAll();
}
