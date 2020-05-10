package es.urjc.ssii.practica3.service;

import es.urjc.ssii.practica3.entity.ReglaAsociacion;
import es.urjc.ssii.practica3.repository.ReglaAsociacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Victor Fernandez Fernandez, Mikayel Mardanyan Petrosyan
 */
@Service
public class ReglaAsociacionService {

    @Autowired
    private ReglaAsociacionRepository asociacionRepository;

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
