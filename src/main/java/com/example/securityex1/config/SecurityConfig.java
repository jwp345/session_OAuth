package com.example.securityex1.config;

import com.example.securityex1.config.auth.oatuh.PrincipalOauth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // secured 어노테이션 활성화, pre/post Authorize 활성화
public class SecurityConfig {

  private final PrincipalOauth2UserService principalOauth2UserService;

  SecurityConfig(PrincipalOauth2UserService principalOauth2UserService) {
    this.principalOauth2UserService = principalOauth2UserService;
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.csrf().disable();
    http.authorizeRequests()
            .antMatchers("/user/**").authenticated()
            .antMatchers("/manager/**").hasAnyRole("ADMIN", "MANAGER")
            .antMatchers("/admin/**").hasRole("ADMIN")
            .anyRequest().permitAll()
            .and()
            .formLogin()
            .loginPage("/loginForm")
            .loginProcessingUrl("/login")
            .defaultSuccessUrl("/")
            .and()
            .oauth2Login()
            .loginPage("/loginForm")
            .userInfoEndpoint()
            .userService(principalOauth2UserService);

    return http.build();
  }
}
