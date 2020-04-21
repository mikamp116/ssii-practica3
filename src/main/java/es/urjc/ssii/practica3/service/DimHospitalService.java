package es.urjc.ssii.practica3.service;

import es.urjc.ssii.practica3.entity.DimHospital;
import es.urjc.ssii.practica3.repository.DimHospitalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class DimHospitalService {

	@Autowired
	private DimHospitalRepository repositorio;

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
