package es.urjc.ssii.practica3.service;

import es.urjc.ssii.practica3.entity.*;
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

import java.io.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author Victor Fernandez Fernandez, Mikayel Mardanyan Petrosyan
 */
@Service
public class FiltradoColaborativoService {

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

    public void filtradoColaborativo() throws IOException, TasteException {
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
                    numeroPacientes[i - 1] = numeroPacientes[i - 1] + 1;  // kahbfñauebfñqiubfaifvapfv
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
        }
        if (f.exists())
            f.delete();
    }
}
