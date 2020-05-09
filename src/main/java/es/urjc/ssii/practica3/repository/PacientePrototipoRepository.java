package es.urjc.ssii.practica3.repository;

import es.urjc.ssii.practica3.entity.PacientePrototipo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Victor Fernandez Fernandez, Mikayel Mardanyan Petrosyan
 */
@Repository
public interface PacientePrototipoRepository extends CrudRepository<PacientePrototipo, Integer> {

    List<PacientePrototipo> findAll();

    List<PacientePrototipo> findByGrupo(String grupo);
}
