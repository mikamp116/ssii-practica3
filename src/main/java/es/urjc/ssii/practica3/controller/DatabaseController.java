package es.urjc.ssii.practica3.controller;

import es.urjc.ssii.practica3.entity.*;
import es.urjc.ssii.practica3.service.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Mikayel Mardanyan Petrosyan
 */
@RestController
public class DatabaseController {

    private final CompuestoRecomendadoService compuestoRecomendadoService;
    private final TablaHechosService hechosService;
    private final DimTiempoService tiempoService;
    private final PacientePrototipoService prototipoService;
    private final ReglaAsociacionService asociacionService;

    public DatabaseController(CompuestoRecomendadoService compuestoRecomendadoService, TablaHechosService tablaHechosService,
                              DimTiempoService tiempoService, PacientePrototipoService prototipoService,
                              ReglaAsociacionService asociacionService) {
        this.compuestoRecomendadoService = compuestoRecomendadoService;
        this.hechosService = tablaHechosService;
        this.tiempoService = tiempoService;
        this.prototipoService = prototipoService;
        this.asociacionService = asociacionService;
    }

    @GetMapping("/compuesto-recomendado")
    public List<CompuestoRecomendado> all() {
        return compuestoRecomendadoService.getAll();
    }

    @GetMapping("/hechos")
    public List<TablaHechos> hechos() {
        return hechosService.getAll();
    }

    @GetMapping("/tiempos")
    public List<DimTiempo> tiempos() {
        return tiempoService.getAll();
    }

    @GetMapping("/fallecidos/{tratamiento}")
    public List<TablaHechos> fallecidos(@PathVariable int tratamiento) {
        return hechosService.getFallecidos(tratamiento);
    }

    @GetMapping("/no_fallecidos/{tratamiento}")
    public List<TablaHechos> noFallecidos(@PathVariable int tratamiento) {
        return hechosService.getNoFallecidos(tratamiento);
    }

    @GetMapping("/num_fallecidos/{tratamiento}")
    public int numFallecidos(@PathVariable int tratamiento) {
        return hechosService.getNumFallecidos(tratamiento);
    }

    @GetMapping("/num_no_fallecidos/{tratamiento}")
    public int numNoFallecidos(@PathVariable int tratamiento) {
        return hechosService.getNumNoFallecidos(tratamiento);
    }

    @GetMapping("/prototipo_uci")
    public List<PacientePrototipo> pacientePrototipoUci() {
        return prototipoService.getPacientesPrototipoUci();
    }

    @GetMapping("/prototipo_fallecido")
    public List<PacientePrototipo> pacientePrototipoFallecido() {
        return prototipoService.getPacientesPrototipoFallecido();
    }

    @GetMapping("/prototipo_resto")
    public List<PacientePrototipo> pacientePrototipoResto() {
        return prototipoService.getPacientesPrototipoResto();
    }

    @GetMapping("/reglas_exito")
    public List<ReglaAsociacion> reglasExito() {
        return asociacionService.getExitos();
    }

    @GetMapping("/reglas_fallo")
    public List<ReglaAsociacion> reglasFallo() {
        return asociacionService.getFallos();
    }
}
