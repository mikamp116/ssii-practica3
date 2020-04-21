package es.urjc.ssii.practica3.service;

import es.urjc.ssii.practica3.repository.DimTiempoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DimTiempoService {

	@Autowired
	private DimTiempoRepository repositorio;
	
}
