package es.urjc.ssii.practica3.service;

import es.urjc.ssii.practica3.entity.ReglaAsociacion;
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

import java.io.File;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

/**
 * @author Mikayel Mardanyan Petrosyan
 */
@Service
public class ReglasAsociacionService {

    private final ReglaAsociacionService asociacionService;

    public ReglasAsociacionService(ReglaAsociacionService asociacionService) {
        this.asociacionService = asociacionService;
    }

    public void reglasAsociacion() throws Exception {

        CSVLoader loader = new CSVLoader();
        loader.setFieldSeparator(",");
        loader.setNoHeaderRowPresent(false);

        // Array con los objetos Instances que va a obtener de cada fichero csv
        Instances[] dataSources = new Instances[4];
        for (int i = 1; i < 5; i++) {
            File source = new File("data/datos_filtrado_colaborativo_" + i + ".csv");
            loader.setSource(source);
            dataSources[i - 1] = loader.getDataSet();
        }
        // Une los distintos Instances en uno solo
        Instances data = merge(dataSources);

        // Borra columna de ids
        Remove r = new Remove();
        r.setAttributeIndices("1");
        r.setInputFormat(data);
        data = Filter.useFilter(data, r);

        // Obtiene las reglas de exito y fallo, pasando como parametro el Instance con toda la informacion anterior
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

        // Escribe las reglas en el fichero
        try (PrintWriter pw = new PrintWriter(new File("entregables/reglasExito.txt"))) {
            pw.println(model);
        }

        // Obtiene las reglas calculadas
        List<AssociationRule> associationRules = model.getAssociationRules().getRules();
        // Para cada regla
        for (AssociationRule rule : associationRules) {
            StringBuilder sb = new StringBuilder();
            // Obtiene las premisas de la implicacion y las guarda en una cadena de caracteres
            Collection<Item> premises = rule.getPremise();
            for (Item premise : premises) {
                sb.append(premise.getAttribute().name().substring(1)).append(',');
            }
            sb.deleteCharAt(sb.length() - 1);
            // Obtiene el consecuente
            String consec = rule.getConsequence().iterator().next().getAttribute().name().substring(1);
            // Obtiene las metricas: conf, lift, lev y conv
            double[] values = rule.getMetricValuesForRule();

            // Crea un objeto y lo guarda en la base de datos
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

        // Escribe las reglas en el fichero
        try (PrintWriter pw = new PrintWriter(new File("entregables/reglasFallo.txt"))) {
            pw.println(model);
        }

        // Obtiene las reglas calculadas
        List<AssociationRule> associationRules = model.getAssociationRules().getRules();
        // Para cada regla
        for (AssociationRule rule : associationRules) {
            StringBuilder sb = new StringBuilder();
            // Obtiene las premisas de la implicacion y las guarda en una cadena de caracteres
            Collection<Item> premises = rule.getPremise();
            for (Item premise : premises) {
                sb.append(premise.getAttribute().name().substring(1)).append(',');
            }
            sb.deleteCharAt(sb.length() - 1);
            // Obtiene el consecuente
            String consec = rule.getConsequence().iterator().next().getAttribute().name().substring(1);
            // Obtiene las metricas: conf, lift, lev y conv
            double[] values = rule.getMetricValuesForRule();

            // Crea un objeto y lo guarda en la base de datos
            ReglaAsociacion ra = new ReglaAsociacion(false, sb.toString(), Integer.parseInt(consec), values[0], values[1], values[2], values[3]);
            asociacionService.save(ra);
        }
    }

    public static double zeroTransformation(double a) {
        return (a == 0) ? 6 : a;
    }
}
