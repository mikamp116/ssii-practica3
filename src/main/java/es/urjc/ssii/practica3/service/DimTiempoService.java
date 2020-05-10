package es.urjc.ssii.practica3.service;

import es.urjc.ssii.practica3.entity.DimTiempo;
import es.urjc.ssii.practica3.repository.DimTiempoRepository;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author Mikayel Mardanyan Petrosyan
 */
@Service
public class DimTiempoService {

    // Las fechas del csv aparecen en diferentes formatos y es necesario unificarlos
    static final DateTimeFormatter formatterYYYY = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    static final DateTimeFormatter formatterYY = DateTimeFormatter.ofPattern("dd/MM/yy");

    private final DimTiempoRepository repositorio;

    public DimTiempoService(DimTiempoRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void save(DimTiempo tiempo) {
        repositorio.save(tiempo);
    }

    public DimTiempo getByFecha(String fecha, DateTimeFormatter formatter) {
        // Se suma el offset en milisegundos de UTC a GMT para obtener la fecha correcta
        return repositorio.findByFecha(new Date(Date.valueOf(LocalDate.parse(fecha, formatter)).getTime() + 8000000));
    }

    public DimTiempo getByFecha(String fecha) {
        // Se suma el offset en milisegundos de UTC a GMT para obtener la fecha correcta
        if (fecha.length() > 9)
            return repositorio.findByFecha(new Date(Date.valueOf(LocalDate.parse(fecha, formatterYYYY)).getTime() + 8000000));
        return repositorio.findByFecha(new Date(Date.valueOf(LocalDate.parse(fecha, formatterYY)).getTime() + 8000000));
    }

    public List<DimTiempo> getAll() {
        return repositorio.findAll();
    }
}
