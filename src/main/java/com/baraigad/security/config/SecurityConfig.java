package com.baraigad.security.config;

import com.baraigad.common.Constants;
import com.baraigad.security.jwt.JwtAuthenticationEntryPoint;
import com.baraigad.security.jwt.JwtAuthenticationFilter;
import com.baraigad.security.filter.AdminMenuAccessFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.Customizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.http.HttpMethod;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter
            jwtAuthenticationFilter;

    private final JwtAuthenticationEntryPoint
            jwtAuthenticationEntryPoint;

    private final AdminMenuAccessFilter adminMenuAccessFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();
    }

    /**
     * Public dynamic-form links must be usable by anyone who receives the link.
     * Excluding this narrowly-scoped route from the JWT filter avoids a login
     * challenge before the public controller can load the form.
     */
    @Bean
    public WebSecurityCustomizer publicFormWebSecurityCustomizer() {
        return web -> web.ignoring().requestMatchers("/api/public/forms/**");
    }

    @Bean
    public AuthenticationManager
    authenticationManager(
            AuthenticationConfiguration config)
            throws Exception {

        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http)
            throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .cors(Customizer.withDefaults())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS))

                .exceptionHandling(ex ->
                        ex.authenticationEntryPoint(
                                jwtAuthenticationEntryPoint))

                .authorizeHttpRequests(auth ->
                        auth

                                .requestMatchers(
                                        Constants.AUTH_URL)
                                .permitAll()
                                .requestMatchers("/error")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/uploads/forms/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/uploads/mohims/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/uploads/forts/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/uploads/gallery/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/uploads/blogs/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/uploads/volunteers/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/uploads/work-banners/**")
                                .permitAll()
                                .requestMatchers("/api/dashboard/homepage","/api/v1/mohim","/api/v1/blog","/api/v1/volunteer-activities/register")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/forts", "/api/v1/forts/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/our-work", "/api/v1/our-work/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/gallery", "/api/v1/gallery/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/v1/work-banners", "/api/v1/work-banners/**")
                                .permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/participants")
                                .permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/contact-enquiries")
                                .permitAll()
                                .requestMatchers(HttpMethod.POST, "/api/v1/website-visits")
                                .permitAll()
                                // Public form links are intentionally accessible without a login.
                                // Keep the entire public API namespace ahead of the authenticated catch-all.
                                .requestMatchers(AntPathRequestMatcher.antMatcher("/api/public/**"))
                                .permitAll()
                                // Super Admin controls identities and can view the immutable audit trail.
                                // Standard Admin users can still submit client audit events, so their actions are recorded.
                                .requestMatchers("/api/v1/admin-audit/client-event")
                                .authenticated()
                                .requestMatchers("/api/v1/admin-menu-access/my")
                                .authenticated()
                                .requestMatchers("/api/v1/admin-menu-access/**")
                                .hasRole("SUPER_ADMIN")
                                .requestMatchers("/api/v1/user/**", "/api/v1/admin-audit/**")
                                .hasRole("SUPER_ADMIN")
                                .anyRequest()
                                .authenticated())

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(
                        adminMenuAccessFilter,
                        JwtAuthenticationFilter.class);

        return http.build();
    }
}
