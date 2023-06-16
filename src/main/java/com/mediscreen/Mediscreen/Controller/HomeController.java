package com.mediscreen.Mediscreen.Controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {


    Logger logger = LoggerFactory.getLogger(HomeController.class);

    @GetMapping("/")
        public String indexPage() {
        logger.info("GET:/");
        return "index";
        }

}
