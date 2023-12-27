package com.rhymthwave.Controller.Admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@CrossOrigin("*")
public class IndexController {

    @GetMapping("/admin")
    public String home(){
        return "/admin/index";
    }
}
