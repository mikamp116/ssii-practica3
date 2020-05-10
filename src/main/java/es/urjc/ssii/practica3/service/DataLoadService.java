package es.urjc.ssii.practica3.service;

import es.urjc.ssii.practica3.entity.DimHospital;
import es.urjc.ssii.practica3.entity.DimPaciente;
import es.urjc.ssii.practica3.entity.DimTiempo;
import es.urjc.ssii.practica3.entity.TablaHechos;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author Mikayel Mardanyan Petrosyan
 */
@Service
public class DataLoadService {

    private final DimHospitalService hospitalService;
    private final DimPacienteService pacienteService;
    private final DimTiempoService tiempoService;
    private final TablaHechosService hechosService;

    public DataLoadService(DimHospitalService hospitalService, DimPacienteService pacienteService, DimTiempoService tiempoService, TablaHechosService hechosService) {
        this.hospitalService = hospitalService;
        this.pacienteService = pacienteService;
        this.tiempoService = tiempoService;
        this.hechosService = hechosService;
    }

    public void loadData() {

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

    public void showData() {
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
}
