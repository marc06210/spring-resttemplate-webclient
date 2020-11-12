/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mgu.oauthclient.authserver.config;

import java.util.UUID;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.keys.KeyManager;
import org.springframework.security.crypto.keys.StaticKeyGeneratingKeyManager;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
@Import(OAuth2AuthorizationServerConfiguration.class)
public class AuthorizationServerConfig {

    // @formatter:off
    @Bean
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("messaging-client")
                .clientSecret("secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .scope("message.read")
                .scope("message.write")
                .build();
        return new InMemoryRegisteredClientRepository(registeredClient);
    }
    // @formatter:on

    @Bean
    public KeyManager keyManager() {
        return new StaticKeyGeneratingKeyManager();
    }

    // @formatter:off
    @Bean
    public UserDetailsService users() {
        User.UserBuilder users = User.withDefaultPasswordEncoder();
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(users.username("user1").password("password").roles("USER").build());
        manager.createUser(users.username("admin").password("password").roles("USER", "ADMIN").build());
        return manager;
    }
    // @formatter:on
}