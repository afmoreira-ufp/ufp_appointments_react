package me.afmiguez.project.ufp_applications;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(method = RequestMethod.GET)
public class CommonController {

    @RequestMapping(value = {"/", "/appointments","/rco2","/rco1","/so","/acpt"})
    public String index() {
        return "index";
    }

    /*@GetMapping("")
    public String getIndex(Model model){
        model.addAttribute("title","Home");
        return "index";
    }

    @GetMapping("/appointments")
    public String getAppointments(Model model) {
        model.addAttribute("title", "Appointments");
        return "appointments";
    }

    @GetMapping("/classes/rco1")
    public String getRCO1(Model model) {
        model.addAttribute("title", "RCO1");
        return "rco1";
    }

    @GetMapping("/classes/esof")
    public String getESOF(Model model) {
        model.addAttribute("title", "ESOF");
        return "esof";
    }


    @GetMapping("/classes/rco2")
    public String getRCO2(Model model) {
        model.addAttribute("title", "RCO1");
        return "rco2";
    }
    @GetMapping("/classes/acpt")
    public String getACPT(Model model) {
        model.addAttribute("title", "ACPT");
        return "acpt";
    }

    @GetMapping("/classes/so")
    public String getSO(Model model) {
        model.addAttribute("title", "SO");
        return "so";
    }
*/
}

