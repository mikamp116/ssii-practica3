package es.urjc.ssii.practica3.service;

import es.urjc.ssii.practica3.dto.PacientePrototipoDTO;
import es.urjc.ssii.practica3.entity.DimPaciente;
import es.urjc.ssii.practica3.repository.DimPacienteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Mikayel Mardanyan Petrosyan
 */
@Service
public class DimPacienteService {

    private final DimPacienteRepository repositorio;

    public DimPacienteService(DimPacienteRepository repositorio) {
        this.repositorio = repositorio;
    }

    public void save(DimPaciente paciente) {
        repositorio.save(paciente);
    }

    public DimPaciente getById(int id) {
        return repositorio.findByPacienteId(id);
    }

    public List<DimPaciente> getAll() {
        return repositorio.findAll();
    }

    public List<PacientePrototipoDTO> getPacientesUci() {
        return repositorio.getPacientesUci();
    }

    public List<PacientePrototipoDTO> getPacientesFallecidos() {
        return repositorio.getPacientesFallecidos();
    }

    public List<PacientePrototipoDTO> getPacientesNoUciNoFallecidos() {
        return repositorio.getPacientesNoUciONoFallecidos();
    }
}
