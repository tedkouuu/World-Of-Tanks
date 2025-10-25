package com.example.world_of_tanks.configuration;

import com.example.world_of_tanks.repositories.UserRepository;
import com.example.world_of_tanks.configService.WorldOfTanksDetailsService;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new Pbkdf2PasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.ignoringAntMatchers("/api/**"))

                .authorizeRequests()

                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()

                .antMatchers("/", "/users/login", "/users/register").permitAll()

                .antMatchers("/assets/**").permitAll()

                .antMatchers("/tanks/info", "/tanks/search", "/maintenance").permitAll()

                .antMatchers("/api/**").permitAll()

                .antMatchers("/pages/admins", "/tanks/add", "/tank/edit", "/tank/delete",
                        "/users/edit", "/users/delete").hasRole("ADMIN")

                .antMatchers("/tanks/battle", "/tanks/delete/all",
                        "/users/tank/edit", "/user/role/tank/edit", "/user/tank/delete").authenticated()

                .anyRequest().authenticated()
                .and()

                .formLogin()
                .loginPage("/users/login").permitAll()
                .usernameParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY)
                .passwordParameter(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_PASSWORD_KEY)
                .defaultSuccessUrl("/users/home")
                .failureForwardUrl("/users/login-error")
                .and()

                .logout()
                .logoutUrl("/users/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository) {
        return new WorldOfTanksDetailsService(userRepository);
    }
}
