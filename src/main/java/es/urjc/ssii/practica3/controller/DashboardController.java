package es.urjc.ssii.practica3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author Victor Fernandez Fernandez
 */
@Controller
public class DashboardController {

    // Muestra el dashboard al ir a localhost:8080
    @RequestMapping("/")
    public String dashboard() {
        return "dashboard";
    }

}
