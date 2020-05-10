package es.urjc.ssii.practica3.repository;

import es.urjc.ssii.practica3.entity.DimTiempo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

/**
 * @author Mikayel Mardanyan Petrosyan
 */
@Repository
public interface DimTiempoRepository extends CrudRepository<DimTiempo, Integer> {

    DimTiempo findByFecha(Date fecha);

    List<DimTiempo> findAll();
}
