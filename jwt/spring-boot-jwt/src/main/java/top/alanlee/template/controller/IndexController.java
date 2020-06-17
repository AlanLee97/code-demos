package top.alanlee.template.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    @GetMapping("/")
    public String hello(){
        return "<center><br><br><h1>Hello Spring Boot Template</h1></center>";
    }

}
