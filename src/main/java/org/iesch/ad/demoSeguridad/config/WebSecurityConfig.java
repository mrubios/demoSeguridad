package org.iesch.ad.demoSeguridad.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {
    @Bean
    public UserDetailsService userDetailsService(){
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withUsername("pepe")
                .password("{noop}" + "0000")
                .roles("USER")
                .build());

        manager.createUser(User.withUsername("admin")
                .password("{noop}" + "1234")
                .roles("USER", "ADMIN")
                .build());


        return manager;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf()
                .disable()
                .authorizeRequests()

                .antMatchers(HttpMethod.DELETE)
                .hasRole("ADMIN")

                .antMatchers("/admin/**")
                .hasAnyRole("ADMIN")

                .antMatchers("/public/**")
                .hasAnyRole("USER", "ADMIN")

                .antMatchers("/login/**")
                .anonymous()
                
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }
}
