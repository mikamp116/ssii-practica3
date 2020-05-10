package es.urjc.ssii.practica3.repository;

import es.urjc.ssii.practica3.entity.DimTiempo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

/**
 * @author Victor Fernandez Fernandez, Mikayel Mardanyan Petrosyan
 */
@Repository
public interface DimTiempoRepository extends CrudRepository<DimTiempo, Integer> {

    DimTiempo findByFecha(Date fecha);

    List<DimTiempo> findAll();

    @Query(value = "SELECT * FROM dim_tiempo WHERE tiempo_id BETWEEN ?1 AND ?2", nativeQuery = true)
    List<DimTiempo> findBetweenIds(int idMin, int idMax);

}
