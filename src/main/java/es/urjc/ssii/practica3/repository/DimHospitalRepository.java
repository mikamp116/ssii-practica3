package es.urjc.ssii.practica3.repository;

import es.urjc.ssii.practica3.entity.DimHospital;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DimHospitalRepository extends CrudRepository<DimHospital, Integer> {

    DimHospital findByHospitalId(int id);

    List<DimHospital> findAll();
}
