package de.ait.myliferando.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.authorizeHttpRequests(auth
                -> auth.requestMatchers("/auth/**")
                .permitAll()
                .requestMatchers(HttpMethod.PATCH, "/orders/*/assign/*")
                .hasRole("DISPATCHER")
                .requestMatchers(HttpMethod.PATCH, "/orders/*/pick-up", "/orders/*/deliver")
                .hasRole("COURIER")
                .requestMatchers(HttpMethod.PATCH, "/orders/*/cancel")
                .hasAnyRole("CLIENT", "DISPATCHER")
                .requestMatchers(HttpMethod.PATCH,
                        "/couriers/*/start-shift", "/couriers/*/end-shift")
                .hasRole("COURIER")
                .requestMatchers(HttpMethod.POST, "/couriers")
                .hasRole("DISPATCHER")
                .requestMatchers(HttpMethod.POST, "/clients")
                .hasRole("DISPATCHER")
                .requestMatchers(HttpMethod.POST, "/orders")
                .hasAnyRole("CLIENT", "DISPATCHER")
                .requestMatchers(HttpMethod.POST,"/users")
                .hasRole("DISPATCHER")
                .anyRequest()
                .authenticated());
        http.httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
