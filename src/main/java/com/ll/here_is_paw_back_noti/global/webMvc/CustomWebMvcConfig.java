package com.ll.here_is_paw_back_noti.global.webMvc;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class CustomWebMvcConfig implements WebMvcConfigurer {

  private final CurrentUserArgumentResolver currentUserArgumentResolver;

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
    resolvers.add(currentUserArgumentResolver);
  }

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry.addMapping("/api/**")
        .allowedOrigins("https://cdpn.io", "http://localhost:5173",
            "https://www.pawpaw.kknaks.site", "https://pawpaw.kknaks.site")
        .allowedMethods("*")
        .allowedHeaders("*")
        .allowCredentials(true);
  }
}