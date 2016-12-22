package org.tarun.learning.tweetnews.trends.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CrossOrigin;

@RestController
@CrossOrigin
public class HealthCheckController {


    @RequestMapping("/trends/api/v1/healthcheck")
    public String health() {
        return "OK";
    }
    @RequestMapping("/loaderio-b4b440f2ebea1f101004c0d45b718f88/")
    public String loadercode() {
        return "loaderio-b4b440f2ebea1f101004c0d45b718f88";
    }
}
