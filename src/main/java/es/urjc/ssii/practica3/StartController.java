package es.urjc.ssii.practica3;

import es.urjc.ssii.practica3.dto.PacientePrototipoDTO;
import es.urjc.ssii.practica3.entity.*;
import es.urjc.ssii.practica3.entity.opcionA.CompuestoRecomendadoH1;
import es.urjc.ssii.practica3.entity.opcionA.CompuestoRecomendadoH2;
import es.urjc.ssii.practica3.entity.opcionA.CompuestoRecomendadoH3;
import es.urjc.ssii.practica3.entity.opcionA.CompuestoRecomendadoH4;
import es.urjc.ssii.practica3.entity.opcionB.CompuestoRecomendado;
import es.urjc.ssii.practica3.service.*;
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
import org.springframework.stereotype.Component;
import weka.associations.Apriori;
import weka.associations.AssociationRule;
import weka.associations.Item;
import weka.clusterers.SimpleKMeans;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericCleaner;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.NumericTransform;
import weka.filters.unsupervised.attribute.Remove;

import javax.annotation.PostConstruct;
import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author Victor Fernandez Fernandez, Mikayel Mardanyan Petrosyan
 */
@Component
public class StartController {
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

    @PostConstruct
    public void start() throws Exception {
        loadData();
//        showData();
        filtradoColaborativo(); // Victor este es el metodo que no debes mirar
        reglasAsociacion();
        clustering();
    }

    private void clustering() throws Exception {

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

    private void showData() {
        StringBuilder sb = new StringBuilder("***** HOSPITALES *****\n______________________\n");
        List<DimHospital> hospitales = hospitalService.getAll();
        for (DimHospital h : hospitales)
            sb.append(h).append("\n");
        System.out.println(sb);

        sb = new StringBuilder("***** TIEMPOS *****\n___________________\n");
        List<DimTiempo> tiempos = tiempoService.getAll();
        for (DimTiempo t : tiempos)
            sb.append(t).append("\n");
        System.out.println(sb);

        sb = new StringBuilder("***** PACIENTES *****\n_____________________\n");
        List<DimPaciente> pacientes = pacienteService.getAll();
        for (DimPaciente p : pacientes)
            sb.append(p).append("\n");
        System.out.println(sb);

        sb = new StringBuilder("***** HECHOS *****\n__________________\n");
        List<TablaHechos> hechos = hechosService.getAll();
        for (TablaHechos h : hechos)
            sb.append(h).append("\n");
        System.out.println(sb);
    }

    private void loadData() {

        String line;

        // DimLugar
        int id = 1;
        try (BufferedReader br = new BufferedReader(new FileReader("data/dimLUGAR.csv"))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] columnas = line.split(";");
                DimHospital hospital = new DimHospital(id, columnas[1], columnas[2], columnas[3], columnas[4]);
                hospitalService.save(hospital);
                id++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // DimTiempo
        id = 1;
        try (BufferedReader br = new BufferedReader(new FileReader("data/dimTIEMPO.csv"))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] columnas = line.split(";");
                DimTiempo tiempo = new DimTiempo(id, columnas[1], columnas[2], columnas[3], columnas[4], columnas[5],
                        columnas[6], columnas[7]);
                tiempoService.save(tiempo);
                id++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // DimPaciente
        id = 1;
        String path = "data/P";
        String ext = ".csv";
        for (int i = 1; i < 3; i++) {
            try (BufferedReader br = new BufferedReader(new FileReader(path + i + ext))) {
                br.readLine();
                while ((line = br.readLine()) != null) {
                    String[] columnas = line.split(";");
                    DimPaciente paciente = new DimPaciente(id, Integer.parseInt(columnas[1]),
                            Byte.parseByte(columnas[2]), Integer.parseInt(columnas[3]), Integer.parseInt(columnas[4]),
                            columnas[5].equals("1"), columnas[6].equals("1"), columnas[7].equals("1"),
                            columnas[8].equals("1"), columnas[9].equals("1"), columnas[10].equals("1"),
                            columnas[11].equals("1"), Integer.parseInt(columnas[12]), columnas[13].equals("1"));
                    pacienteService.save(paciente);
                    id++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try (BufferedReader br = new BufferedReader(new FileReader(path + 3 + ext))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] columnas = line.split(";");
                DimPaciente paciente = new DimPaciente(id, Integer.parseInt(columnas[1]),
                        (byte) (columnas[2].equals("M") ? 1 : 0), Integer.parseInt(columnas[3]),
                        Integer.parseInt(columnas[4]), !columnas[5].equals("No"), !columnas[6].equals("No"),
                        !columnas[7].equals("No"), !columnas[8].equals("No"), !columnas[9].equals("No"),
                        !columnas[10].equals("No"), !columnas[11].equals("No"), Integer.parseInt(columnas[13]),
                        !columnas[12].equals("No"));
                pacienteService.save(paciente);
                id++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try (BufferedReader br = new BufferedReader(new FileReader(path + 4 + ext))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] columnas = line.split(";");
                DimPaciente paciente = new DimPaciente(id, Integer.parseInt(columnas[1]),
                        (byte) (columnas[2].equals("M") ? 1 : 0), Integer.parseInt(columnas[3]),
                        Integer.parseInt(columnas[4]), !columnas[5].equals("No"), !columnas[6].equals("No"),
                        !columnas[7].equals("No"), !columnas[8].equals("No"), !columnas[9].equals("No"),
                        !columnas[10].equals("No"), !columnas[11].equals("No"), Integer.parseInt(columnas[12]),
                        !columnas[13].equals("No"));
                pacienteService.save(paciente);
                id++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // TablaHechos
        id = 1;
        path = "data/H";
        DateTimeFormatter formatterYYYY = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter formatterYY = DateTimeFormatter.ofPattern("dd/MM/yy");
        DimHospital hospital = hospitalService.getById(1);
        try (BufferedReader br = new BufferedReader(new FileReader(path + 1 + ext))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] columnas = line.split(";");
                TablaHechos hechos = new TablaHechos(id, pacienteService.getById(id), hospital,
                        tiempoService.getByFecha(columnas[2], formatterYYYY), Integer.parseInt(columnas[3]),
                        !columnas[4].equals("No"), !columnas[5].equals("No"), Integer.parseInt(columnas[6]));
                hechosService.save(hechos);
                id++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        hospital = hospitalService.getById(2);
        try (BufferedReader br = new BufferedReader(new FileReader(path + 2 + ext))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] columnas = line.split(";");
                TablaHechos hechos = new TablaHechos(id, pacienteService.getById(id), hospital,
                        tiempoService.getByFecha(columnas[2]), Integer.parseInt(columnas[3]),
                        !columnas[4].equals("No"), !columnas[5].equals("No"), Integer.parseInt(columnas[6]));
                hechosService.save(hechos);
                id++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        hospital = hospitalService.getById(3);
        try (BufferedReader br = new BufferedReader(new FileReader(path + 3 + ext))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] columnas = line.split(";");
                TablaHechos hechos = new TablaHechos(id, pacienteService.getById(id), hospital,
                        tiempoService.getByFecha(columnas[2], formatterYYYY), Integer.parseInt(columnas[3]),
                        !columnas[4].equals("No"), !columnas[5].equals("No"), Integer.parseInt(columnas[6]));
                hechosService.save(hechos);
                id++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        hospital = hospitalService.getById(4);
        try (BufferedReader br = new BufferedReader(new FileReader(path + 4 + ext))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] columnas = line.split(";");
                TablaHechos hechos = new TablaHechos(id, pacienteService.getById(id), hospital,
                        tiempoService.getByFecha(columnas[2], formatterYY), Integer.parseInt(columnas[3]),
                        !columnas[4].equals("No"), !columnas[5].equals("No"), Integer.parseInt(columnas[6]));
                hechosService.save(hechos);
                id++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* No mires!!! */
    private void filtradoColaborativo() throws IOException, TasteException {
        String line;
        String path = "data/datos_filtrado_colaborativo_";
        String ext = ".csv";
        String sep = ",";
        int cont = 0;
        int[] numeroPacientes = new int[]{0, 0, 0, 0};
        BufferedWriter writer = new BufferedWriter(new FileWriter("data/datos_filtrado_colaborativo_todos.csv", false));
        /* QUE NO MIREEES */
        for (int i = 1; i < 5; i++) {
            if (i > 1)
                cont += numeroPacientes[i - 2];
            try (BufferedReader br = new BufferedReader(new FileReader(path + i + ext))) {
                br.readLine();
                while ((line = br.readLine()) != null) {
                    String[] columnas = line.split(",");
                    numeroPacientes[i - 1] = numeroPacientes[i - 1] + 1;
                    for (int j = 0; j < columnas.length; j++)
                        if (j != 0 && !columnas[j].equals("0"))
                            writer.append(String.valueOf(Integer.parseInt(columnas[0]) + cont)).append(sep)
                                    .append(String.valueOf(j)).append(sep).append(columnas[j]).append("\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writer.close();
        cont += numeroPacientes[3];


        int[] items = new int[3];
        float[] values = new float[3];
        File f = new File("data/datos_filtrado_colaborativo_todos.csv");
        DataModel model = new FileDataModel(f);
        UserSimilarity similarity = new CityBlockSimilarity(model);
        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
        UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
        for (long i = 1L; i <= cont; i++) {
            List<RecommendedItem> userRecommendations = recommender.recommend(i, 3);
            for (int j = 0; j < 3; j++) {
                if (userRecommendations.size() > j) {
                    items[j] = (int) userRecommendations.get(j).getItemID();
                    values[j] = userRecommendations.get(j).getValue();
                } else {
                    items[j] = 0;
                    values[j] = 0;
                }
            }

            /* Apartado 4.4 - Opcion A: Inicio */
            if (i <= numeroPacientes[0]) {
                CompuestoRecomendadoH1 cr = new CompuestoRecomendadoH1((int) i, items[0], values[0], items[1], values[1],
                        items[2], values[2]);
                compuestoRecomendadoService.saveHospital1(cr);
            } else {
                if (i <= (numeroPacientes[1] + numeroPacientes[0])) {
                    CompuestoRecomendadoH2 cr = new CompuestoRecomendadoH2((int) i - numeroPacientes[0], items[0],
                            values[0], items[1], values[1], items[2], values[2]);
                    compuestoRecomendadoService.saveHospital2(cr);
                } else {
                    if (i <= (numeroPacientes[0] + numeroPacientes[1] + numeroPacientes[2])) {
                        CompuestoRecomendadoH3 cr = new CompuestoRecomendadoH3(
                                (int) i - numeroPacientes[0] - numeroPacientes[1], items[0], values[0], items[1],
                                values[1], items[2], values[2]);
                        compuestoRecomendadoService.saveHospital3(cr);
                    } else {
                        CompuestoRecomendadoH4 cr = new CompuestoRecomendadoH4(
                                (int) i - numeroPacientes[0] - numeroPacientes[1] - numeroPacientes[2], items[0],
                                values[0], items[1], values[1], items[2], values[2]);
                        compuestoRecomendadoService.saveHospital4(cr);
                    }
                }
            }
            /* Apartado 4.4 - Opcion A: Fin */

            /* Apartado 4.4 - Opcion B: Inicio */
            int hospitalId;

            if (i <= numeroPacientes[0]) {
                hospitalId = 1;
            } else {
                if (i <= (numeroPacientes[1] + numeroPacientes[0])) {
                    hospitalId = 2;
                } else {
                    if (i <= (numeroPacientes[0] + numeroPacientes[1] + numeroPacientes[2])) {
                        hospitalId = 3;
                    } else {
                        hospitalId = 4;
                    }
                }
            }

            CompuestoRecomendado cr = new CompuestoRecomendado((int) i, hospitalId, items[0], values[0], items[1],
                    values[1], items[2], values[2]);
            compuestoRecomendadoService.save(cr);
            /* Apartado 4.4 - Opcion B: Fin */

        }
        if (f.exists())
            f.delete();
    }

    private void reglasAsociacion() throws Exception {

        CSVLoader loader = new CSVLoader();
        loader.setFieldSeparator(",");
        loader.setNoHeaderRowPresent(false);

        Instances[] dataSources = new Instances[4];
        for (int i = 1; i < 5; i++) {
            File source = new File("data/datos_filtrado_colaborativo_" + i + ".csv");
            loader.setSource(source);
            dataSources[i - 1] = loader.getDataSet();
        }
        Instances data = merge(dataSources);

        // Borra columna de ids
        Remove r = new Remove();
        r.setAttributeIndices("1");
        r.setInputFormat(data);
        data = Filter.useFilter(data, r);

        reglasExito(new Instances(data));
        reglasFallo(data);
    }

    public static Instances merge(Instances[] datas) throws Exception {
        Instances dest = new Instances(datas[0]);
        StringBuilder rel = new StringBuilder();
        for (int i = 0; i < datas.length; i++) {
            rel.append(datas[i].relationName());
            if (i != datas.length - 1)
                rel.append("+");
        }
        dest.setRelationName(String.valueOf(rel));

        for (int i = 1; i < datas.length; i++) {
            ConverterUtils.DataSource source = new ConverterUtils.DataSource(datas[i]);
            Instances instances = source.getStructure();
            Instance instance;
            while (source.hasMoreElements(instances)) {
                instance = source.nextElement(instances);
                dest.add(instance);
            }
        }

        return dest;
    }

    private void reglasExito(Instances data) throws Exception {
        // Inserta los missing values
        NumericCleaner nc = new NumericCleaner();
        nc.setMinThreshold(3.5);
        nc.setMinDefault(Double.NaN);
        nc.setMaxThreshold(3.5);
        nc.setMaxDefault(1);
        nc.setInputFormat(data);
        data = Filter.useFilter(data, nc);

        NumericToNominal nn = new NumericToNominal();
        nn.setInputFormat(data);
        data = Filter.useFilter(data, nn);

        Apriori model = new Apriori();

        model.buildAssociations(data);
        try (PrintWriter pw = new PrintWriter(new File("data/reglasExito.txt"))) {
            pw.println(model);
        }
        List<AssociationRule> b = model.getAssociationRules().getRules();
        for (AssociationRule a : b) {
            StringBuilder sb = new StringBuilder();
            Collection<Item> premises = a.getPremise();
            for (Item premise : premises) {
                sb.append(premise.getAttribute().name().substring(1)).append(',');
            }
            sb.deleteCharAt(sb.length() - 1);
            String consec = a.getConsequence().iterator().next().getAttribute().name().substring(1);
            double[] values = a.getMetricValuesForRule();
            ReglaAsociacion ra = new ReglaAsociacion(true, sb.toString(), Integer.parseInt(consec), values[0], values[1], values[2], values[3]);
            asociacionService.save(ra);
        }
    }

    private void reglasFallo(Instances data) throws Exception {
        // Tranformacion de ceros en un numero mayor a 3
        NumericTransform nt = new NumericTransform();
        nt.setAttributeIndices("first-last");
        nt.setClassName("es.urjc.ssii.practica3.StartController");
        nt.setMethodName("zeroTransformation");
        nt.setInputFormat(data);
        data = Filter.useFilter(data, nt);

        // Inserta los missing values
        NumericCleaner nc = new NumericCleaner();
        nc.setMinThreshold(3.5);
        nc.setMinDefault(1);
        nc.setMaxThreshold(3.5);
        nc.setMaxDefault(Double.NaN);
        nc.setInputFormat(data);
        data = Filter.useFilter(data, nc);

        NumericToNominal nn = new NumericToNominal();
        nn.setInputFormat(data);
        data = Filter.useFilter(data, nn);

        Apriori model = new Apriori();

        model.buildAssociations(data);
        try (PrintWriter pw = new PrintWriter(new File("data/reglasFallo.txt"))) {
            pw.println(model);
        }
        List<AssociationRule> b = model.getAssociationRules().getRules();
        for (AssociationRule a : b) {
            StringBuilder sb = new StringBuilder();
            Collection<Item> premises = a.getPremise();
            for (Item premise : premises) {
                sb.append(premise.getAttribute().name().substring(1)).append(',');
            }
            sb.deleteCharAt(sb.length() - 1);
            String consec = a.getConsequence().iterator().next().getAttribute().name().substring(1);
            double[] values = a.getMetricValuesForRule();
            ReglaAsociacion ra = new ReglaAsociacion(false, sb.toString(), Integer.parseInt(consec), values[0], values[1], values[2], values[3]);
            asociacionService.save(ra);
        }
    }

    public static double zeroTransformation(double a) {
        return (a == 0) ? 6 : a;
    }
}