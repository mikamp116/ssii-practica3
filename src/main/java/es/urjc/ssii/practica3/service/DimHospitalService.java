package es.urjc.ssii.practica3.service;

import es.urjc.ssii.practica3.entity.DimHospital;
import es.urjc.ssii.practica3.repository.DimHospitalRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Mikayel Mardanyan Petrosyan
 */
@Service
public class DimHospitalService {

    private final DimHospitalRepository repositorio;

    public DimHospitalService(DimHospitalRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void save(DimHospital hospital) {
        repositorio.save(hospital);
    }

    public DimHospital getById(int id) {
        return repositorio.findByHospitalId(id);
    }

    public List<DimHospital> getAll() {
        return repositorio.findAll();
    }
}
