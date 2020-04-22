package es.urjc.ssii.practica3;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ChartsController {

    @RequestMapping("/")
    public String charts(){ return "charts"; }

}
