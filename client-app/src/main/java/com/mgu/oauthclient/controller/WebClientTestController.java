package com.mgu.oauthclient.controller;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.core.OAuth2AuthorizationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestController
@Profile("webclient")
public class WebClientTestController {

    private static final Logger LOG = LoggerFactory.getLogger(WebClientTestController.class);

    private WebClient webClient;

    public WebClientTestController(WebClient webClient) {
        this.webClient = webClient;
    }

    @GetMapping("/test")
    public String testW(@RequestParam(name = "client", defaultValue = "client-ok") String client,
            @RequestParam(name = "url", defaultValue = "messages") String url) {
        LOG.info("{}", webClient);
        return this.webClient.get().uri("http://localhost:8092/" + url).attributes(clientRegistrationId(client))
                .retrieve()
//        .onStatus(HttpStatus::is5xxServerError,  clientResponse -> Mono.error(new MyCustomServerException()))
                .bodyToMono(String.class).block();
    }

    @ExceptionHandler(OAuth2AuthorizationException.class)
    public ResponseEntity<String> handleWebClientResponseException(OAuth2AuthorizationException ex) {
        LOG.error("Error from WebClient: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

//    @ExceptionHandler(WebClientResponseException.class)
//    public ResponseEntity<String> handleWebClientResponseException(WebClientResponseException ex) {
//        LOG.error("Error from WebClient - Status {}, Body {}: {}", ex.getRawStatusCode(), ex.getResponseBodyAsString(), ex);
//        return ResponseEntity.status(HttpStatus.OK).body(ex.getResponseBodyAsString());
//    }
//    
//    @ExceptionHandler(MyCustomServerException.class)
//    public ResponseEntity<String> handleMyCustomServerException(MyCustomServerException ex) {
//        return ResponseEntity.status(HttpStatus.OK).body(ex.getMessage());
//    }
//    
//    @ExceptionHandler(NullPointerException.class)
//    public ResponseEntity<String> handleWebClientResponseException(NullPointerException ex) {
//        if(ex!=null && ex.getStackTrace().length >0
//                && ex.getStackTrace()[0].getFileName().equals("DefaultClientCredentialsTokenResponseClient.java")
//                && ex.getStackTrace()[0].getMethodName().equals("getTokenResponse")) {
//            return ResponseEntity.status(HttpStatus.OK).body("auth error detected");
//        } else {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
//        }
//    }
//    
//    @ExceptionHandler(IllegalStateException.class)
//    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
//        LOG.error("IllegalStateException from RestTemplate: ", ex);
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
//    }

}
