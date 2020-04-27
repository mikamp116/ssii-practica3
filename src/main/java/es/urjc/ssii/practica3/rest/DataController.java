package es.urjc.ssii.practica3.rest;

import es.urjc.ssii.practica3.dto.TablaHechosDTO;
import es.urjc.ssii.practica3.entity.TablaHechos;
import es.urjc.ssii.practica3.repository.TablaHechosRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Comparator;
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
                 .map(TablaHechosDTO::convertTablaHechosToDTO).
                 sorted(Comparator.comparing(o -> o.getFechaIngreso().getFecha())).
                 filter(o -> o.getFechaIngreso().getFecha().isBefore(LocalDate.now())).
                 collect(Collectors.toList());
    }
}
