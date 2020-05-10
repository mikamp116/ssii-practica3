package es.urjc.ssii.practica3;

import es.urjc.ssii.practica3.service.ClusteringService;
import es.urjc.ssii.practica3.service.DataLoadService;
import es.urjc.ssii.practica3.service.FiltradoColaborativoService;
import es.urjc.ssii.practica3.service.ReglasAsociacionService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Mikayel Mardanyan Petrosyan
 */
@Component
public class Start {

    private final DataLoadService dataLoadService;
    private final FiltradoColaborativoService filtradoColaborativoService;
    private final ReglasAsociacionService reglasAsociacionService;
    private final ClusteringService clusteringService;

    public Start(DataLoadService dataLoadService, FiltradoColaborativoService filtradoColaborativoService,
                 ReglasAsociacionService reglasAsociacionService, ClusteringService clusteringService) {
        this.dataLoadService = dataLoadService;
        this.filtradoColaborativoService = filtradoColaborativoService;
        this.reglasAsociacionService = reglasAsociacionService;
        this.clusteringService = clusteringService;
    }

    @PostConstruct
    public void start() throws Exception {

        // Apartado 2
        dataLoadService.loadData();
        // Apartado 4
        filtradoColaborativoService.filtradoColaborativo();
        // Apartado 5
        reglasAsociacionService.reglasAsociacion();
        // Apartado 6
        clusteringService.clustering();
    }
}