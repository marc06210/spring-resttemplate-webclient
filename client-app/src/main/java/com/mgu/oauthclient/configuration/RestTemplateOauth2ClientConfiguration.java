package com.mgu.oauthclient.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;

import com.mgu.oauthclient.properties.RestTemplateSecurityCredentials;

@Configuration
@Profile("resttemplate")
public class RestTemplateOauth2ClientConfiguration {
    
    private RestTemplateSecurityCredentials oauth2Configuration;
    
    public RestTemplateOauth2ClientConfiguration(RestTemplateSecurityCredentials oauth2Configuration) {
        this.oauth2Configuration = oauth2Configuration;
    }
    
    @Bean("client-ok")
    public OAuth2RestTemplate messagingClientClientCredsRestTemplate(/*
            @Qualifier("messagingClientOkClientCredsDetails") OAuth2ProtectedResourceDetails resourceDetails*/) {
        return new OAuth2RestTemplate(oauth2Configuration.getClientCredentialsResourceDetails("client-ok"));
    }
    
    @Bean("client-ko")
    public OAuth2RestTemplate messagingClientKoClientCredsRestTemplate(/*
            @Qualifier("messagingClientKoClientCredsDetails") OAuth2ProtectedResourceDetails resourceDetails*/) {
        return new OAuth2RestTemplate(oauth2Configuration.getClientCredentialsResourceDetails("client-ko"));
    }
    
    /*
    @ConfigurationProperties(prefix = "security.oauth2.client.client-ok")
    @Bean
    public OAuth2ProtectedResourceDetails messagingClientOkClientCredsDetails() {
        return new ClientCredentialsResourceDetails();
    }
    
    @ConfigurationProperties(prefix = "security.oauth2.client.client-ko")
    @Bean
    public OAuth2ProtectedResourceDetails messagingClientKoClientCredsDetails() {
        return new ClientCredentialsResourceDetails();
    }*/
    

}
