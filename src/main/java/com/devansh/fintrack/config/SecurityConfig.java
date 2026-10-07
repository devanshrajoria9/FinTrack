    package com.devansh.fintrack.config;

    import com.devansh.fintrack.filter.JwtAuthenticationFilter;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.http.HttpMethod;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
    import org.springframework.security.crypto.password.PasswordEncoder;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
    import org.springframework.stereotype.Component;

    @Configuration
    public class SecurityConfig {


        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
            this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        }

        @Bean
        public PasswordEncoder passwordEncoder(){
            return new BCryptPasswordEncoder();
            }

        @Bean
        public AuthenticationManager authenticationManager(
               AuthenticationConfiguration configuration)
               throws Exception{

        return configuration.getAuthenticationManager();

    }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http)
                throws Exception{

            http.csrf(csrf -> csrf.disable());

            http.authorizeHttpRequests(auth -> auth
                    .requestMatchers("/auth/login").permitAll()
                    .requestMatchers(HttpMethod.POST, "/user").permitAll()
                    .anyRequest().authenticated()
            );

            http.addFilterBefore(
                    jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter.class
            );

            return http.build();
        }
    }
