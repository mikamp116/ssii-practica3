package es.urjc.ssii.practica3.repository;

import es.urjc.ssii.practica3.entity.DimTiempo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DimTiempoRepository extends CrudRepository<DimTiempo, Integer> {
}
