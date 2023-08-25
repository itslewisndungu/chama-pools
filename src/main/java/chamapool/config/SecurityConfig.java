package chamapool.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity security) throws Exception {
    return security
        .cors()
        .and()
        .csrf(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt)
        .authorizeHttpRequests()
        .requestMatchers("/auth/**")
        .permitAll()
        .requestMatchers(HttpMethod.POST, "/members/invites/{inviteId}/accept")
        .permitAll()
        .requestMatchers(HttpMethod.GET, "/members/invites/{inviteId}")
        .permitAll()
        .requestMatchers(HttpMethod.GET, "/members", "/members/**")
        .permitAll()
        .requestMatchers("/reports/**")
        .permitAll()
        .anyRequest()
        .authenticated()
        .and()
        .sessionManagement(
            manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
