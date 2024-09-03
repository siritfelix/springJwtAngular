package com.coderio.task.shared.configuration;

import static org.springframework.security.config.http.SessionCreationPolicy.*;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.coderio.task.controller.AuthenticationController;
import com.coderio.task.service.UserService;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserService userService;
    private final AuthHandlerEntryPointJwt authHandlerEntryPointJwt;
    private String[] resources = new String[] { AuthenticationController.PATH.concat(AuthenticationController.SINGUP),
            AuthenticationController.PATH.concat(AuthenticationController.SINGIN),
            AuthenticationController.PATH.concat(AuthenticationController.REFRESH),
            AuthenticationController.PATH.concat(AuthenticationController.LOGOUT),
            "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html",
            "/swagger-ui/index.html", "/webjars/**", "/include/**", "/js/**", "/css/**" };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable).cors(c -> corsFilter())
                .exceptionHandling(error -> error.authenticationEntryPoint(authHandlerEntryPointJwt))
                .authorizeHttpRequests(
                        request -> request
                                .requestMatchers(resources)
                                .permitAll().anyRequest().authenticated())
                .sessionManagement(manager -> manager.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider()).addFilterBefore(
                        jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("http://localhost:4200");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(new AntPathRequestMatcher("/h2-console/**"));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService.userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config)
            throws Exception {
        return config.getAuthenticationManager();
    }

    /*
     * @Bean
     * public WebMvcConfigurer corsConfigurer() {
     * return new WebMvcConfigurer() {
     * 
     * @Override
     * public void addCorsMappings(CorsRegistry registry) {
     * registry.addMapping("/**")
     * .allowedOrigins("http://localhost:8081")
     * .allowedMethods("GET", "POST", "PUT", "DELETE","PATCH", "OPTIONS")
     * .allowedHeaders("*")
     * .allowCredentials(true);
     * }
     * };
     * }/*
     */
    /*
     * @Bean
     * public CorsConfigurationSource corsConfigurationSource() {
     * UrlBasedCorsConfigurationSource source = new
     * UrlBasedCorsConfigurationSource();
     * CorsConfiguration config = new CorsConfiguration();
     * config.setAllowedOrigins(List.of("http://127.0.0.1:4200",
     * "http://127.0.0.1:4200/*", "http://localhost:4200",
     * "http://localhost:4200/**"));
     * config.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT",
     * "PATCH"));
     * config.addAllowedHeader("*");
     * config.setAllowCredentials(true);
     * source.registerCorsConfiguration("/**", config);
     * return source;
     * }
     */

}
