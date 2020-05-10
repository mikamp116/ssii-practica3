package es.urjc.ssii.practica3.service;

import es.urjc.ssii.practica3.entity.ReglaAsociacion;
import es.urjc.ssii.practica3.repository.ReglaAsociacionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Mikayel Mardanyan Petrosyan
 */
@Service
public class ReglaAsociacionService {

    private final ReglaAsociacionRepository asociacionRepository;

    public ReglaAsociacionService(ReglaAsociacionRepository asociacionRepository) {
        this.asociacionRepository = asociacionRepository;
    }

    public List<ReglaAsociacion> getExitos() {
        return asociacionRepository.findByExito(true);
    }

    public List<ReglaAsociacion> getFallos() {
        return asociacionRepository.findByExito(false);
    }

    public void save(ReglaAsociacion ra) {
        asociacionRepository.save(ra);
    }
}
