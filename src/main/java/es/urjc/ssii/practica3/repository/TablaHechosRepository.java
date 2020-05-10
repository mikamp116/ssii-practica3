package es.urjc.ssii.practica3.repository;

import es.urjc.ssii.practica3.entity.TablaHechos;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Victor Fernandez Fernandez, Mikayel Mardanyan Petrosyan
 */
@Repository
public interface TablaHechosRepository extends CrudRepository<TablaHechos, Integer> {

    List<TablaHechos> findAll();

    List<TablaHechos> findByTratamientoAndFallecido(Integer tratamiento, boolean fallecido);

    Integer countByTratamientoAndFallecido(Integer tratamiento, boolean fallecido);
}
