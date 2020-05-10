package es.urjc.ssii.practica3.service;

import es.urjc.ssii.practica3.dto.PacientePrototipoDTO;
import es.urjc.ssii.practica3.entity.PacientePrototipo;
import org.springframework.stereotype.Service;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mikayel Mardanyan Petrosyan
 */
@Service
public class ClusteringService {

    private final DimPacienteService pacienteService;
    private final PacientePrototipoService prototipoService;

    public ClusteringService(DimPacienteService pacienteService, PacientePrototipoService prototipoService) {
        this.pacienteService = pacienteService;
        this.prototipoService = prototipoService;
    }

    public void clustering() throws Exception {

        // Listas con los posibles valores que pueden tomar los atributos
        List<String> booleans = new ArrayList<>(Arrays.asList("No", "Si"));
        List<String> sexo = new ArrayList<>(Arrays.asList("V", "M"));
        List<String> numericRange = new ArrayList<>(Arrays.asList("0", "1", "2", "3"));

        // Crea los atributos de los pacieentes y les asina sus posibles valores
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

        // Objeto Instances que almacena los pacientes con sus atributos
        Instances data = new Instances("Pacientes prototipo", attInfo, pacientes.size());
        // Cada paciente se guarda en un objeto DenseInstance, una implementacion de Instance
        for (PacientePrototipoDTO p : pacientes) {
            DenseInstance di = new DenseInstance(1.0, p.toArray());
            di.setDataset(data);
            data.add(di);
        }

        // Numero de pacientes prototipo
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
        // Objeto Instances con los pacientes prototipo obtenidos
        Instances pacientesPrototipo = kmeans.getClusterCentroids();

        // Guarda en un fichero las caracteristicas de los prototipos
        try (PrintWriter pw = new PrintWriter(new File("entregables/" + filename + ".txt"))) {
            for (int i = 0; i < pacientesPrototipo.size(); i++) {
                // Para cada paciente prototipo
                Instance paciente = pacientesPrototipo.instance(i);
                pw.print("Paciente prototipo " + i + ": ");
                // y para cada uno de sus atributos
                for (int j = 0; j < paciente.numAttributes(); j++) {
                    // Guarda el atributo y su valor en el fichero
                    if (attInfo.get(j).isNominal())  // si es nominal, obtiene el valor del array de posibles valores
                        pw.print(attInfo.get(j).name() + "=" + attInfo.get(j).value((int) paciente.value(j)));
                    else  // si no lo es, sera numerico y guardara el valor de tipo double
                        pw.print(attInfo.get(j).name() + "=" + String.format("%.1f", paciente.value(j)));
                    if (j != paciente.numAttributes() - 1)
                        pw.print(", ");
                    else
                        pw.print("\n");
                }

                // Crea un paciente prototipo para almacenar en la base de datos
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
