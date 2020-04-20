package es.urjc.ssii.practica3.repository;

import es.urjc.ssii.practica3.entity.DimHospital;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DimHospitalRepository extends CrudRepository<DimHospital, Integer> {
}
