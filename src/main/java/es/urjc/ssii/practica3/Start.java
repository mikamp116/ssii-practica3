package es.urjc.ssii.practica3;

import es.urjc.ssii.practica3.repository.TablaHechosRepository;
import es.urjc.ssii.practica3.service.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Victor Fernandez Fernandez, Mikayel Mardanyan Petrosyan
 */
@Component
public class Start {

    private final DataLoadService dataLoadService;
    private final FiltradoColaborativoService filtradoColaborativoService;
    private final ReglasAsociacionService reglasAsociacionService;
    private final ClusteringService clusteringService;
    private final TablaHechosRepository tablaHechosRepository;

    public Start(DataLoadService dataLoadService, FiltradoColaborativoService filtradoColaborativoService, ReglasAsociacionService reglasAsociacionService, ClusteringService clusteringService, TablaHechosRepository tablaHechosRepository) {
        this.dataLoadService = dataLoadService;
        this.filtradoColaborativoService = filtradoColaborativoService;
        this.reglasAsociacionService = reglasAsociacionService;
        this.clusteringService = clusteringService;
        this.tablaHechosRepository = tablaHechosRepository;
    }

    @PostConstruct
    public void start() throws Exception {
        if (tablaHechosRepository.count() == 0){
            dataLoadService.loadData();
            filtradoColaborativoService.filtradoColaborativo();
            reglasAsociacionService.reglasAsociacion();
            clusteringService.clustering();
        }
    }
}