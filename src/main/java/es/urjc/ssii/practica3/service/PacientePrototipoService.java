package es.urjc.ssii.practica3.service;

import es.urjc.ssii.practica3.entity.PacientePrototipo;
import es.urjc.ssii.practica3.repository.PacientePrototipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Mikayel Mardanyan Petrosyan
 */
@Service
public class PacientePrototipoService {

    @Autowired
    private PacientePrototipoRepository repositorio;

    public void save(PacientePrototipo paciente) {
        repositorio.save(paciente);
    }

    public List<PacientePrototipo> getAll() {
        return repositorio.findAll();
    }

    public List<PacientePrototipo> getPacientesPrototipoUci() {
        return repositorio.findByGrupo("Uci");
    }

    public List<PacientePrototipo> getPacientesPrototipoFallecido() {
        return repositorio.findByGrupo("Fallecido");
    }

    public List<PacientePrototipo> getPacientesPrototipoResto() {
        return repositorio.findByGrupo("Resto");
    }
}
