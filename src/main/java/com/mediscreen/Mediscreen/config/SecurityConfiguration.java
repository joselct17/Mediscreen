package com.mediscreen.Mediscreen.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
    /**
     * Spring Security needs to have a PasswordEncoder defined.
     * @return PasswordEncoder that uses the BCrypt
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/**").permitAll()
                        .requestMatchers("/login/**").permitAll()
                        .requestMatchers("/registration/**").permitAll()
                        .requestMatchers("/usertransaction/**").hasAnyRole("USER")
                        .requestMatchers("/banktransaction/**").hasAnyRole("USER")
                        .requestMatchers("/connection/**").hasAnyRole("USER")
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/")
                        .permitAll()

                )
//               .oauth2Login((form) ->form
//                    .defaultSuccessUrl("/usertransaction")
//                    .loginPage("/login")
//
//                )

                .logout((logout) -> logout.permitAll()
                        .logoutSuccessUrl("/")
                );

        return http.build();
    }
}
