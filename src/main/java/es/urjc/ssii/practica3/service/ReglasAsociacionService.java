package es.urjc.ssii.practica3.service;

import es.urjc.ssii.practica3.entity.CompuestoRecomendado;
import es.urjc.ssii.practica3.entity.ReglaAsociacion;
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
import weka.associations.Apriori;
import weka.associations.AssociationRule;
import weka.associations.Item;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericCleaner;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.NumericTransform;
import weka.filters.unsupervised.attribute.Remove;

import java.io.*;
import java.util.Collection;
import java.util.List;

/**
 * @author Victor Fernandez Fernandez, Mikayel Mardanyan Petrosyan
 */
@Service
public class ReglasAsociacionService {

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

    public void reglasAsociacion() throws Exception {

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
        nt.setClassName("es.urjc.ssii.practica3.service.ReglasAsociacionService");
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
