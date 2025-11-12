package com.jacek.nutriweek.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpHeaders.*;
import static org.springframework.http.HttpMethod.*;

@Slf4j
@Configuration
public class CorsConfig {

    private  static final String X_XSRF_TOKEN = "X-XSRF-TOKEN";
    private  static final String XSRF_TOKEN = "XSRF-TOKEN";

    @Value("${app.cors.allowed-origins}")
    private String allowedOrigins;

    @Bean
    public CorsFilter corsFilter(){
        log.info("CORS allowed origins: {}", Arrays.asList(allowedOrigins.split(",")));

        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        config.setAllowedHeaders(List.of(ORIGIN, CONTENT_TYPE, ACCEPT, X_XSRF_TOKEN, XSRF_TOKEN));
        config.setExposedHeaders(List.of("Set-Cookie", XSRF_COOKIE));

        config.setAllowedMethods(List.of(
                GET.name(), POST.name(), PUT.name(), PATCH.name(), DELETE.name(), OPTIONS.name()
        ));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}
