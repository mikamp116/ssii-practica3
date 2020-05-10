package es.urjc.ssii.practica3.repository;

import es.urjc.ssii.practica3.entity.ReglaAsociacion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Victor Fernandez Fernandez, Mikayel Mardanyan Petrosyan
 */
@Repository
public interface ReglaAsociacionRepository extends CrudRepository<ReglaAsociacion, Integer> {

    List<ReglaAsociacion> findByExito(boolean bool);
}
