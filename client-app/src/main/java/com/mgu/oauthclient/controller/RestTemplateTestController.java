package com.mgu.oauthclient.controller;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Profile("resttemplate")
public class RestTemplateTestController {

    private ApplicationContext context;
    
    public RestTemplateTestController(ApplicationContext context) {
        this.context = context;
    }
    
    @GetMapping("/test")
    public String testRestTemplate(@RequestParam(name="client", defaultValue = "client-ok") String client, @RequestParam(name="url", defaultValue = "messages") String url) {
        OAuth2RestTemplate restTemplate = context.getBean(client, OAuth2RestTemplate.class);
        return restTemplate.getForObject("http://localhost:8092/"+url, String.class);
    }
    
}
