package es.urjc.ssii.practica3.service;

import es.urjc.ssii.practica3.dto.PacientePrototipoDTO;
import es.urjc.ssii.practica3.entity.CompuestoRecomendado;
import es.urjc.ssii.practica3.entity.PacientePrototipo;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.CityBlockSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Victor Fernandez Fernandez, Mikayel Mardanyan Petrosyan
 */
@Service
public class ClusteringService {

    @Autowired
    private DimHospitalService hospitalService;
    @Autowired
    private DimPacienteService pacienteService;
    @Autowired
    private DimTiempoService tiempoService;
    @Autowired
    private TablaHechosService hechosService;
    @Autowired
    private CompuestoRecomendadoService compuestoRecomendadoService;
    @Autowired
    private PacientePrototipoService prototipoService;
    @Autowired
    private ReglaAsociacionService asociacionService;

    public void clustering() throws Exception {

        List<String> booleans = new ArrayList<>(Arrays.asList("No", "Si"));
        List<String> sexo = new ArrayList<>(Arrays.asList("V", "M"));
        List<String> numericRange = new ArrayList<>(Arrays.asList("0", "1", "2", "3"));

        ArrayList<Attribute> attInfo = new ArrayList<>();
        attInfo.add(new Attribute("Edad"));
        attInfo.add(new Attribute("Sexo", sexo));
        attInfo.add(new Attribute("IMC"));
        attInfo.add(new Attribute("Forma física", numericRange));
        attInfo.add(new Attribute("Tabaquismo", booleans));
        attInfo.add(new Attribute("Alcoholismo", booleans));
        attInfo.add(new Attribute("Colesterol", booleans));
        attInfo.add(new Attribute("Hipertensión", booleans));
        attInfo.add(new Attribute("Cardiopatia", booleans));
        attInfo.add(new Attribute("Reuma", booleans));
        attInfo.add(new Attribute("EPOC", booleans));
        attInfo.add(new Attribute("Hepatitis", numericRange));
        attInfo.add(new Attribute("Cáncer", booleans));

        getPacientesPrototipo(pacienteService.getPacientesUci(), attInfo, "protoUci");
        getPacientesPrototipo(pacienteService.getPacientesFallecidos(), attInfo, "protoFallecido");
        getPacientesPrototipo(pacienteService.getPacientesNoUciNoFallecidos(), attInfo, "protoResto");
    }

    private void getPacientesPrototipo(List<PacientePrototipoDTO> pacientes, ArrayList<Attribute> attInfo, String filename)
            throws Exception {
        Instances data = new Instances("Pacientes prototipo", attInfo, pacientes.size());
        for (PacientePrototipoDTO p : pacientes) {
            DenseInstance di = new DenseInstance(1.0, p.toArray());
            di.setDataset(data);
            data.add(di);
        }

        int K = 3;
        int maxIteration = 100;
        SimpleKMeans kmeans = new SimpleKMeans();
        kmeans.setNumClusters(K);
        kmeans.setMaxIterations(maxIteration);
        kmeans.setPreserveInstancesOrder(true);
        try {
            kmeans.buildClusterer(data);
        } catch (Exception ex) {
            System.err.println("Unable to build Clusterer: " + ex.getMessage());
            ex.printStackTrace();
        }
        Instances pacientesPrototipo = kmeans.getClusterCentroids();

        try (PrintWriter pw = new PrintWriter(new File("data/" + filename + ".txt"))) {
            for (int i = 0; i < pacientesPrototipo.size(); i++) {
                Instance paciente = pacientesPrototipo.instance(i);
                pw.print("Paciente prototipo " + i + ": ");
                for (int j = 0; j < paciente.numAttributes(); j++) {
                    if (attInfo.get(j).isNominal())
                        pw.print(attInfo.get(j).name() + "=" + attInfo.get(j).value((int) paciente.value(j)));
                    else
                        pw.print(attInfo.get(j).name() + "=" + String.format("%.1f", paciente.value(j)));
                    if (j != paciente.numAttributes() - 1)
                        pw.print(", ");
                    else
                        pw.print("\n");
                }

                PacientePrototipo pp = new PacientePrototipo((int) paciente.value(0), (byte) paciente.value(1),
                        (int) paciente.value(2), (int) paciente.value(3), paciente.value(4) == 1,
                        paciente.value(5) == 1, paciente.value(6) == 1,
                        paciente.value(7) == 1, paciente.value(8) == 1,
                        paciente.value(9) == 1, paciente.value(10) == 1,
                        (int) paciente.value(11), paciente.value(12) == 1, filename.substring(5));
                prototipoService.save(pp);
            }
        }
    }
}
