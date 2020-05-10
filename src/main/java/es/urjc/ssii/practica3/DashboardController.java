package es.urjc.ssii.practica3;

import es.urjc.ssii.practica3.entity.DimTiempo;
import es.urjc.ssii.practica3.entity.PacientePrototipo;
import es.urjc.ssii.practica3.entity.ReglaAsociacion;
import es.urjc.ssii.practica3.entity.TablaHechos;
import es.urjc.ssii.practica3.entity.opcionA.CompuestoRecomendadoH1;
import es.urjc.ssii.practica3.entity.opcionB.CompuestoRecomendado;
import es.urjc.ssii.practica3.repository.DimTiempoRepository;
import es.urjc.ssii.practica3.repository.TablaHechosRepository;
import es.urjc.ssii.practica3.repository.opcionA.CompuestoRecomendadoH1Repository;
import es.urjc.ssii.practica3.repository.opcionB.CompuestoRecomendadoRepository;
import es.urjc.ssii.practica3.service.PacientePrototipoService;
import es.urjc.ssii.practica3.service.ReglaAsociacionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Victor Fernandez Fernandez, Mikayel Mardanyan Petrosyan
 */
@RestController
public class DashboardController {

    private final CompuestoRecomendadoH1Repository repositoryH1;
    private final CompuestoRecomendadoRepository repository;
    private final TablaHechosRepository hechosRepository;
    private final DimTiempoRepository tiempoRepository;
    private final PacientePrototipoService prototipoService;
    private final ReglaAsociacionService asociacionService;

    public DashboardController(CompuestoRecomendadoH1Repository repositoryH1, CompuestoRecomendadoRepository repository,
                               TablaHechosRepository hechosRepository, DimTiempoRepository tiempoRepository,
                               PacientePrototipoService prototipoService, ReglaAsociacionService asociacionService) {
        this.repositoryH1 = repositoryH1;
        this.repository = repository;
        this.hechosRepository = hechosRepository;
        this.tiempoRepository = tiempoRepository;
        this.prototipoService = prototipoService;
        this.asociacionService = asociacionService;
    }

    @GetMapping("/compuesto-recomendado1")
    public List<CompuestoRecomendadoH1> h1() {

        return repositoryH1.findAll();
    }

    @GetMapping("/compuesto-recomendado")
    public List<CompuestoRecomendado> all() {

        return repository.findAll();
    }


    @GetMapping("/hechos")
    public List<TablaHechos> hechos() {

        return hechosRepository.findAll();
    }

    @GetMapping("/tiempos")
    public List<DimTiempo> tiempos() {

        return tiempoRepository.findAll();
    }

    @GetMapping("/fallecidos/{tratamiento}")
    public List<TablaHechos> fallecidos(@PathVariable int tratamiento) {

        return hechosRepository.findByTratamientoAndFallecido(tratamiento, true);
    }

    @GetMapping("/no_fallecidos/{tratamiento}")
    public List<TablaHechos> noFallecidos(@PathVariable int tratamiento) {

        return hechosRepository.findByTratamientoAndFallecido(tratamiento, false);
    }

    @GetMapping("/num_fallecidos/{tratamiento}")
    public int numFallecidos(@PathVariable Integer tratamiento) {

        return hechosRepository.countByTratamientoAndFallecido(tratamiento, true);
    }

    @GetMapping("/num_no_fallecidos/{tratamiento}")
    public int numNoFallecidos(@PathVariable Integer tratamiento) {

        return hechosRepository.countByTratamientoAndFallecido(tratamiento, false);
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
