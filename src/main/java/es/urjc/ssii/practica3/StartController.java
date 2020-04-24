package es.urjc.ssii.practica3;

import es.urjc.ssii.practica3.entity.DimHospital;
import es.urjc.ssii.practica3.entity.DimPaciente;
import es.urjc.ssii.practica3.entity.DimTiempo;
import es.urjc.ssii.practica3.entity.TablaHechos;
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
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Discretize;
import weka.filters.unsupervised.attribute.NumericCleaner;
import weka.filters.unsupervised.attribute.NumericTransform;
import weka.filters.unsupervised.attribute.Remove;

import javax.annotation.PostConstruct;
import java.io.*;
import java.time.format.DateTimeFormatter;
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

    @PostConstruct
    public void start() throws Exception {
        loadData();
        showData();
        filtradoColaborativo(); // Victor este es el metodo que no debes mirar
        reglasAsociacion();
    }

    public static Instances merge(Instances data1, Instances[] datas) throws Exception {
        Instances dest = new Instances(data1);
        dest.setRelationName(data1.relationName() + "+" + datas[0].relationName() + "+" + datas[1].relationName() + "+" +
                datas[2].relationName());

        for (Instances data : datas) {
            ConverterUtils.DataSource source = new ConverterUtils.DataSource(data);
            Instances instances = source.getStructure();
            Instance instance = null;
            while (source.hasMoreElements(instances)) {
                instance = source.nextElement(instances);
                dest.add(instance);
            }
        }

        return dest;
    }

    public static Instances merge(Instances[] datas) throws Exception {
        Instances dest = new Instances(datas[0]);
        StringBuilder rel = new StringBuilder();
        for (int i = 0; i < datas.length; i++) {
            rel.append(datas[i].relationName());
            if (i != datas.length-1)
                rel.append("+");
        }
        System.out.println(rel);
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

    private void reglasAsociacion() throws Exception {

        CSVLoader loader = new CSVLoader();
        loader.setFieldSeparator(",");
        loader.setNoHeaderRowPresent(false);

        String path = "data/datos_filtrado_colaborativo_";
        String ext = ".csv";
        Instances [] data_sources = new Instances[4];
        for (int i = 1; i < 5; i++) {
            File source = new File(path + i + ext);
            loader.setSource(source);
            data_sources[i-1] = loader.getDataSet();
        }

        /*File source1 = new File("data/datos_filtrado_colaborativo_1.csv");
        loader.setSource(source1);
        Instances data1 = loader.getDataSet();

        File source2 = new File("data/datos_filtrado_colaborativo_2.csv");
        loader.setSource(source2);
        Instances data2 = loader.getDataSet();

        File source3 = new File("data/datos_filtrado_colaborativo_3.csv");
        loader.setSource(source3);
        Instances data3 = loader.getDataSet();

        File source4 = new File("data/datos_filtrado_colaborativo_4.csv");
        loader.setSource(source4);
        Instances data4 = loader.getDataSet();

        Instances data = merge(data1, new Instances[]{data2, data3, data4});*/
        Instances data = merge(data_sources);

        // Borra columna de ids
        Remove r = new Remove();
        r.setAttributeIndices("1");
        r.setInputFormat(data);
        data = Filter.useFilter(data, r);

        // Tranformacion de valores
        NumericTransform nt = new NumericTransform();
        nt.setAttributeIndices("first-last");
        nt.setClassName("es.urjc.ssii.practica3.StartController");
        nt.setMethodName("transf");
        nt.setInputFormat(data);
        data = Filter.useFilter(data, nt);

        /*//- String to Nominal
        StringToNominal filter1 = new StringToNominal();
        filter1.setAttributeRange("first-last");
        filter1.setInputFormat(data);
        data = Filter.useFilter(data, filter1);*/

        /*NumericToNominal filter5 = new NumericToNominal();
        filter5.setAttributeIndices("first-last");
        filter5.setInputFormat(data);
        Instances dataB = Filter.useFilter(data, filter5);*/

        // Inserta los missing values
        NumericCleaner nc = new NumericCleaner();
        nc.setMinThreshold(0.9);
        nc.setMinDefault(Double.NaN);
        nc.setAttributeIndices("first-last");
        nc.setInputFormat(data);
        data = Filter.useFilter(data, nc);

        // Discretiza en 2 cubos
        Discretize disc = new Discretize();
        disc.setBins(2);
        disc.setInputFormat(data);
        data = Filter.useFilter(data, disc);

        //- Mostrar en pantalla los atributos de los datos
//        System.out.println(loader.getStructure() + " ...\n\n");

        Apriori model = new Apriori();

        model.buildAssociations(data);
        System.out.println(model);
    }

    public static double transf(double a) {
        if (a > 3)
            return 1;
        return 0;
    }/*

    public static String transf(String a) {
        if (Integer.parseInt(a) > 3)
            return "t";
        return "?";
    }*/

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
        try (BufferedReader br = new BufferedReader(new FileReader(path + 1 + ext))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] columnas = line.split(";");
                TablaHechos hechos = new TablaHechos(id, pacienteService.getById(id), hospitalService.getById(id),
                        tiempoService.getByFecha(columnas[2], formatterYYYY), Integer.parseInt(columnas[3]),
                        !columnas[4].equals("No"), !columnas[5].equals("No"), Integer.parseInt(columnas[6]));
                hechosService.save(hechos);
                id++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(path + 2 + ext))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] columnas = line.split(";");
                TablaHechos hechos = new TablaHechos(id, pacienteService.getById(id), hospitalService.getById(id),
                        tiempoService.getByFecha(columnas[2]), Integer.parseInt(columnas[3]),
                        !columnas[4].equals("No"), !columnas[5].equals("No"), Integer.parseInt(columnas[6]));
                hechosService.save(hechos);
                id++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(path + 3 + ext))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] columnas = line.split(";");
                TablaHechos hechos = new TablaHechos(id, pacienteService.getById(id), hospitalService.getById(id),
                        tiempoService.getByFecha(columnas[2], formatterYYYY), Integer.parseInt(columnas[3]),
                        !columnas[4].equals("No"), !columnas[5].equals("No"), Integer.parseInt(columnas[6]));
                hechosService.save(hechos);
                id++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(path + 4 + ext))) {
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] columnas = line.split(";");
                TablaHechos hechos = new TablaHechos(id, pacienteService.getById(id), hospitalService.getById(id),
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
        DataModel model = new FileDataModel(new File("data/datos_filtrado_colaborativo_todos.csv"));
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
        File f = new File("data/datos_filtrado_colaborativo_todos.csv");
        if (f.exists())
            f.delete();
    }
}