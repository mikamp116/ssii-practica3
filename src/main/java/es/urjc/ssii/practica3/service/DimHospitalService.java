package es.urjc.ssii.practica3.service;

import es.urjc.ssii.practica3.repository.DimHospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class DimHospitalService {

	@Autowired
	private DimHospitalRepository repositorio;

}
