package es.urjc.ssii.practica3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DashboardController {

    @RequestMapping("/")
    public String dashboard() {
        return "dashboard";
    }

    @RequestMapping("/charts")
    public String charts() {
        return "charts";
    }

}
