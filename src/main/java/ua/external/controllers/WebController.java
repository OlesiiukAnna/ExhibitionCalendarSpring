package ua.external.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import static ua.external.controllers.Paths.INDEX_FILE;

@RequestMapping("/")
@Controller
public class WebController {

    @GetMapping("/")
    public String getIndex() {
        return INDEX_FILE;
    }

}
