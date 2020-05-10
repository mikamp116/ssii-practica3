package es.urjc.ssii.practica3.controller;

import es.urjc.ssii.practica3.entity.DimTiempo;
import es.urjc.ssii.practica3.entity.PacientePrototipo;
import es.urjc.ssii.practica3.entity.ReglaAsociacion;
import es.urjc.ssii.practica3.entity.TablaHechos;
import es.urjc.ssii.practica3.entity.CompuestoRecomendado;
import es.urjc.ssii.practica3.repository.DimTiempoRepository;
import es.urjc.ssii.practica3.repository.TablaHechosRepository;
import es.urjc.ssii.practica3.repository.CompuestoRecomendadoRepository;
import es.urjc.ssii.practica3.service.PacientePrototipoService;
import es.urjc.ssii.practica3.service.ReglaAsociacionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import weka.Run;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Victor Fernandez Fernandez, Mikayel Mardanyan Petrosyan
 */
@RestController
public class DatabaseController {

    private final CompuestoRecomendadoRepository repository;
    private final TablaHechosRepository hechosRepository;
    private final DimTiempoRepository tiempoRepository;
    private final PacientePrototipoService prototipoService;
    private final ReglaAsociacionService asociacionService;

    public DatabaseController(CompuestoRecomendadoRepository repository, TablaHechosRepository hechosRepository, DimTiempoRepository tiempoRepository,
                              PacientePrototipoService prototipoService, ReglaAsociacionService asociacionService) {
        this.repository = repository;
        this.hechosRepository = hechosRepository;
        this.tiempoRepository = tiempoRepository;
        this.prototipoService = prototipoService;
        this.asociacionService = asociacionService;
    }

    @GetMapping("/compuesto-recomendado")
    public List<CompuestoRecomendado> all() {

        return repository.findAll();
    }

    @GetMapping("/numeros")
    public List<Integer> getNumeros() {
        List<TablaHechos> hechos = hechos();
        List<Integer> numeros = new ArrayList<>();

        numeros.add(hechos.size());
        numeros.add(Math.toIntExact(hechos.stream().filter(TablaHechos::isUci).count()));
        numeros.add(Math.toIntExact(hechos.stream().filter(TablaHechos::isFallecido).count()));
        return numeros;
    }


    @GetMapping("/hechos")
    public List<TablaHechos> hechos() {

        return hechosRepository.findAll();
    }

    @GetMapping("/tiempos")
    public List<DimTiempo> tiempos() {

        int primerIngresoFechaId = getPrimerIngresoFechaId();
        int ultimaAltaFechaId = getUltimaAltaFechaId();

        return tiempoRepository.findBetweenIds(primerIngresoFechaId, ultimaAltaFechaId);
    }

    private int getPrimerIngresoFechaId(){
        List<TablaHechos> hechos = hechos();
        TablaHechos hechoOptional = hechos.stream().min(Comparator.comparingInt(h -> h.getFechaIngresoId().getTiempoId())).
                orElse(null);

        if (hechoOptional == null){
            throw new RuntimeException("Tabla de hechos vacia.");
        }

        return hechoOptional.getFechaIngresoId().getTiempoId();
    }

    private int getUltimaAltaFechaId(){
        List<TablaHechos> hechos = hechos();
        TablaHechos hechoOptional = hechos.stream().max(Comparator.comparingInt(h -> h.getFechaIngresoId().getTiempoId() + h.getDuracion())).
                orElse(null);

        if (hechoOptional == null){
            throw new RuntimeException("Tabla de hechos vacia.");
        }

        return hechoOptional.getFechaIngresoId().getTiempoId() + hechoOptional.getDuracion();
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
