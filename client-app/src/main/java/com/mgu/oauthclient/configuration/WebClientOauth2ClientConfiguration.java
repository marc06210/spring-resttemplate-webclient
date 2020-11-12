package com.mgu.oauthclient.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientProviderBuilder;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import com.mgu.oauthclient.controller.MyCustomServerException;

import reactor.core.publisher.Mono;

@Configuration
@Profile("webclient")
public class WebClientOauth2ClientConfiguration {
    
    private static final Logger LOG = LoggerFactory.getLogger(WebClientOauth2ClientConfiguration.class);
    @Bean
    WebClient webClient(OAuth2AuthorizedClientManager authorizedClientManager) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Client = new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        return WebClient.builder()
                .apply(oauth2Client.oauth2Configuration())
                .filter(logRequests())
                .filter(logResponseStatus())
                .filter(handleExceptions())
                .build();
    }
    
    private ExchangeFilterFunction handleExceptions() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            if(clientResponse.statusCode()!=null && (clientResponse.statusCode().is5xxServerError() || clientResponse.statusCode().is4xxClientError()) ) {
                 return clientResponse.bodyToMono(String.class)
                         .switchIfEmpty(Mono.error(new MyCustomServerException()))
                         .flatMap(errorBody -> Mono.error(new MyCustomServerException(errorBody)));
//                         .onErrorResume((th) -> Mono.error(new MyCustomServerException(th))) ;
            }else {
                return Mono.just(clientResponse);
            }
        });
    }
    
    private ExchangeFilterFunction logResponseStatus() {
        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
            LOG.info("Response Status {}", clientResponse.statusCode());
            return Mono.just(clientResponse);
        });
    }
 
    private ExchangeFilterFunction logRequests() {
        return (clientRequest, next) -> {
            LOG.info("Request: {} {}", clientRequest.method(), clientRequest.url());
            clientRequest.headers()
                    .forEach((name, values) -> values.forEach(value -> LOG.info("{}={}", name, value)));
            return next.exchange(clientRequest);
        };
    }

    @Bean
    OAuth2AuthorizedClientManager authorizedClientManager(ClientRegistrationRepository clientRegistrationRepository, OAuth2AuthorizedClientRepository authorizedClientRepository) {
        OAuth2AuthorizedClientProvider authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder.builder()/*.authorizationCode().refreshToken()*/.clientCredentials()/*.password()*/.build();
        DefaultOAuth2AuthorizedClientManager authorizedClientManager = new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientRepository);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
        return authorizedClientManager;
    }

}
