    package com.devansh.fintrack.config;

    import com.devansh.fintrack.filter.JwtAuthenticationFilter;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.http.HttpMethod;
    import org.springframework.http.HttpStatus;
    import org.springframework.security.authentication.AuthenticationManager;
    import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;

    import org.springframework.security.config.http.SessionCreationPolicy;
    import org.springframework.security.web.SecurityFilterChain;
    import org.springframework.security.web.authentication.HttpStatusEntryPoint;
    import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


    @Configuration
    public class SecurityConfig {


        private final JwtAuthenticationFilter jwtAuthenticationFilter;

        private final GoogleOAuth2SuccessHandler googleOAuth2SuccessHandler;

        public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, GoogleOAuth2SuccessHandler googleOAuth2SuccessHandler) {
            this.jwtAuthenticationFilter = jwtAuthenticationFilter;
            this.googleOAuth2SuccessHandler = googleOAuth2SuccessHandler;
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
                    .requestMatchers("/auth/register", "/auth/login").permitAll()
                    .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()
                    .requestMatchers(HttpMethod.POST, "/user").permitAll()
                    .anyRequest().authenticated()

            );

            http.exceptionHandling(exception -> exception
                    .defaultAuthenticationEntryPointFor(
                            new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                            request -> request.getRequestURI().startsWith("/user")
                    )
            );

            http.oauth2Login(oauth -> oauth
                    .successHandler(googleOAuth2SuccessHandler)
            );

            http.addFilterBefore(
                    jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter.class
            );

            http.sessionManagement(session -> session
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            );

            return http.build();
        }


    }
