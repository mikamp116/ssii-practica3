package es.urjc.ssii.practica3.repository;

import es.urjc.ssii.practica3.entity.TablaHechos;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TablaHechosRepository extends CrudRepository<TablaHechos, Integer> {
}
