package es.urjc.ssii.practica3.service;

import es.urjc.ssii.practica3.entity.DimTiempo;
import es.urjc.ssii.practica3.repository.DimTiempoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author Victor Fernandez Fernandez, Mikayel Mardanyan Petrosyan
 */
@Service
public class DimTiempoService {

    static final DateTimeFormatter formatterYYYY = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    static final DateTimeFormatter formatterYY = DateTimeFormatter.ofPattern("dd/MM/yy");

    @Autowired
    private DimTiempoRepository repositorio;

    public void save(DimTiempo tiempo) {
        repositorio.save(tiempo);
    }

    public DimTiempo getByFecha(String fecha, DateTimeFormatter formatter) {
        return repositorio.findByFecha(new Date(Date.valueOf(LocalDate.parse(fecha, formatter)).getTime() + 8000000));
    }

    public DimTiempo getByFecha(String fecha) {
        if (fecha.length() > 9)
            return repositorio.findByFecha(new Date(Date.valueOf(LocalDate.parse(fecha, formatterYYYY)).getTime() + 8000000));
        return repositorio.findByFecha(new Date(Date.valueOf(LocalDate.parse(fecha, formatterYY)).getTime() + 8000000));
    }

    public List<DimTiempo> getAll() {
        return repositorio.findAll();
    }
}
