package es.urjc.ssii.practica3.repository;

import es.urjc.ssii.practica3.entity.CompuestoRecomendado;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

/**
 * @author Victor Fernandez Fernandez, Mikayel Mardanyan Petrosyan
 */
@RepositoryRestResource(collectionResourceRel = "compuesto-recomendado", path = "compuesto-recomendado")
public interface CompuestoRecomendadoRepository extends CrudRepository<CompuestoRecomendado, Integer> {

    List<CompuestoRecomendado> findAll();
}
