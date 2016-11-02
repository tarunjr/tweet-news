package org.tarun.learning.tweetnews.trends.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.tarun.learning.tweetnews.trends.model.Url;
import org.tarun.learning.tweetnews.trends.service.UrlService;

import java.util.List;

@CrossOrigin
@RequestMapping("/api/v1/urls")
public class UrlController {

    @Autowired
    private final UrlService urlService;

    public UrlController(UrlService service) {
        this.urlService = service;
    }
    @RequestMapping("/")
    public List<Url> urls() {
        return urlService.getAll();
    }
    @RequestMapping("/top/{k}")
    public List<Url> topUrls(@PathVariable(value="k") int k) {
        return urlService.getTop(k);
    }
}
