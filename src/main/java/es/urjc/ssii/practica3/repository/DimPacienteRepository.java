package es.urjc.ssii.practica3.repository;

import es.urjc.ssii.practica3.entity.DimPaciente;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DimPacienteRepository extends CrudRepository<DimPaciente, Integer> {
}
