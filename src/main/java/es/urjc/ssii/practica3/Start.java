package es.urjc.ssii.practica3;

import es.urjc.ssii.practica3.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author Victor Fernandez Fernandez, Mikayel Mardanyan Petrosyan
 */
@Component
public class Start {
    @Autowired
    private DataLoadService dataLoadService;
    @Autowired
    private FiltradoColaborativoService filtradoColaborativoService;
    @Autowired
    private ReglasAsociacionService reglasAsociacionService;
    @Autowired
    private ClusteringService clusteringService;

    @PostConstruct
    public void start() throws Exception {
        dataLoadService.loadData();
        filtradoColaborativoService.filtradoColaborativo();
        reglasAsociacionService.reglasAsociacion();
        clusteringService.clustering();
    }
}