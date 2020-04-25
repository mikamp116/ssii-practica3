package es.urjc.ssii.practica3.rest;

import es.urjc.ssii.practica3.dto.TablaHechosDTO;
import es.urjc.ssii.practica3.entity.TablaHechos;
import es.urjc.ssii.practica3.repository.TablaHechosRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class DataController {

    private final TablaHechosRepository tablaHechosRepository;

    public DataController(TablaHechosRepository tablaHechosRepository) {
        this.tablaHechosRepository = tablaHechosRepository;
    }

    @GetMapping(value = "/hechos", produces = "application/json")
    public List<TablaHechosDTO> getTablaHechos(){
         return tablaHechosRepository.findAll().stream()
                 .map(TablaHechosDTO::convertTablaHechosToDTO).collect(Collectors.toList());
    }
}
