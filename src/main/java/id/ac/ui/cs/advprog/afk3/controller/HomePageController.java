package id.ac.ui.cs.advprog.afk3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class HomePageController {
    String listHTML = "HelloWorld";
    @GetMapping
    public String homePage(){
        return listHTML;
    }
}
