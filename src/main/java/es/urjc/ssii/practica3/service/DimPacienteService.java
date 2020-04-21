package es.urjc.ssii.practica3.service;

import es.urjc.ssii.practica3.repository.DimPacienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DimPacienteService {

	@Autowired
	private DimPacienteRepository repositorio;

}
