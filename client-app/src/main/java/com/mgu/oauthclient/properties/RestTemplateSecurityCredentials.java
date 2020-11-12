package com.mgu.oauthclient.properties;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * This class holds into a HashMap all ClientCredentialsResourceDetails
 * defined under security.oauth2.client in the yml file.
 * 
 * <pre>
 * security:
  oauth2:
    client:
      client-ok:
        client-id: messaging-client
        client-secret: secret
        grant-type: client_credentials
        access-token-uri: http://localhost:8090/oauth/token
        scope: message.read, message.write
      client-ko:
        client-id: messaging-client
        client-secret: secretM
        grant-type: client_credentials
        access-token-uri: http://localhost:8090/oauth/token
        scope: message.read, message.write
 * </pre>
 * @author marc
 *
 */
@Component
@ConfigurationProperties(prefix = "security.oauth2")
public class RestTemplateSecurityCredentials extends ClientCredentialsResourceDetails {
    
    private static final String MISSING_FIELD_ERROR_LOG = "Missing %s configuration for %s";
    
    private final Map<String, ClientCredentialsResourceDetails> client = new HashMap<>();

    public Map<String, ClientCredentialsResourceDetails> getClient() {
        return client;
    }

    /**
     * Convenient methd that return the ClientCredentialsResourceDetails by to its name.
     * @param clientName
     * @return null if not found
     */
    public ClientCredentialsResourceDetails getClientCredentialsResourceDetails(String clientName) {
        return client.get(clientName);
    }
    
    @PostConstruct
    public void validate() {
        this.getClient().values().forEach(this::validateClientCredentialsResourceDetails);
    }

    private void validateClientCredentialsResourceDetails(ClientCredentialsResourceDetails configuration) {
        if (StringUtils.isEmpty(configuration.getClientSecret())) {
            throw new IllegalStateException(String.format(MISSING_FIELD_ERROR_LOG, "secret", 
                    configuration.getClientId()));
        }
        // grant_type is not checked because it is defaulted to client_credentials
        if (StringUtils.isEmpty(configuration.getAccessTokenUri())) {
            throw new IllegalStateException(String.format(MISSING_FIELD_ERROR_LOG, "access-token-uri", 
                    configuration.getClientId()));
        }
        if (StringUtils.isEmpty(configuration.getScope())) {
            throw new IllegalStateException(String.format(MISSING_FIELD_ERROR_LOG, "scope", 
                    configuration.getClientId()));
        }
    }

}
