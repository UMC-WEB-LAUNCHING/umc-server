package com.umc.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReactTestController {

    Logger log= LoggerFactory.getLogger(ReactTestController.class);
    @GetMapping("/home")
    public String getHome(){
        log.info(">>>>ReactTestController");
        return "Hello wolrd!";
    }
}
