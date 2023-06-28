package io.flowing.retail.productservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    /**
     * For the backend-resources, I indicate that all the endpoints are protected.
     * To request any endpoint, the OAuth2 protocol is necessary, using the server configured and with the given scope.
     * Thus, a JWT will be used to communicate between the backend-resources and backend-auth when backend-resources
     * needs to validate the authentication of a request.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.authorizeRequests()
            .antMatchers(HttpMethod.GET, "/**").hasAuthority("SCOPE_message.read")
            .antMatchers(HttpMethod.POST, "/**").hasAuthority("SCOPE_message.write")
            .antMatchers(HttpMethod.PUT, "/**").hasAuthority("SCOPE_message.write")
            .antMatchers(HttpMethod.DELETE, "/**").hasAuthority("SCOPE_message.write")
            .anyRequest().authenticated()
            .and()
            .oauth2ResourceServer().jwt();

        return http.build();
    }
}
