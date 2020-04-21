package es.urjc.ssii.practica3.repository;

import es.urjc.ssii.practica3.entity.TablaHechos;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TablaHechosRepository extends CrudRepository<TablaHechos, Integer> {

    List<TablaHechos> findAll();
}
