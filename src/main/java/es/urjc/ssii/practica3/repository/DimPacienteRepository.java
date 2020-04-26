package es.urjc.ssii.practica3.repository;

import es.urjc.ssii.practica3.dto.PacientePrototipoDTO;
import es.urjc.ssii.practica3.entity.DimPaciente;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Victor Fernandez Fernandez, Mikayel Mardanyan Petrosyan
 */
@Repository
public interface DimPacienteRepository extends CrudRepository<DimPaciente, Integer> {

    DimPaciente findByPacienteId(int id);

    List<DimPaciente> findAll();

    @Query(value="select new es.urjc.ssii.practica3.dto.PacientePrototipoDTO(p.edad, p.sexo, p.imc, p.formaFisica, " +
            "p.tabaquismo, p.alcoholismo, p.colesterol, p.hipertension, p.cardiopatia, p.reuma, p.epoc, p.hepatitis, " +
            "p.cancer) from DimPaciente p join TablaHechos th on p = th.pacienteId where th.uci = true ")
    List<PacientePrototipoDTO> getPacientesUci();

    @Query(value="select new es.urjc.ssii.practica3.dto.PacientePrototipoDTO(p.edad, p.sexo, p.imc, p.formaFisica, " +
            "p.tabaquismo, p.alcoholismo, p.colesterol, p.hipertension, p.cardiopatia, p.reuma, p.epoc, p.hepatitis, " +
            "p.cancer) from DimPaciente p join TablaHechos th on p = th.pacienteId where th.fallecido = true ")
    List<PacientePrototipoDTO> getPacientesFallecidos();

    @Query(value="select new es.urjc.ssii.practica3.dto.PacientePrototipoDTO(p.edad, p.sexo, p.imc, p.formaFisica, " +
            "p.tabaquismo, p.alcoholismo, p.colesterol, p.hipertension, p.cardiopatia, p.reuma, p.epoc, p.hepatitis, " +
            "p.cancer) from DimPaciente p join TablaHechos th on p = th.pacienteId where th.uci = false and th.fallecido = false")
    List<PacientePrototipoDTO> getPacientesNoUciONoFallecidos();
}
