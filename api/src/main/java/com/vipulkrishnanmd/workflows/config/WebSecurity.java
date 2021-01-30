package com.vipulkrishnanmd.workflows.config;

import com.vipulkrishnanmd.workflows.auth.JWTAuthenticationFilter;
import com.vipulkrishnanmd.workflows.auth.JWTAuthorizationFilter;
import com.vipulkrishnanmd.workflows.service.UserDetailsServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    public WebSecurity(BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailsServiceImpl userDetailsService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        JWTAuthenticationFilter authenticationFilter = new JWTAuthenticationFilter(authenticationManager());
        // Change default auth URL
        authenticationFilter.setFilterProcessesUrl("/api/auth/v1/login");

        http.cors().and().csrf().disable().authorizeRequests().antMatchers(HttpMethod.POST, "/api/user/v1/user")
                .permitAll().anyRequest().authenticated().and().addFilter(authenticationFilter)
                .addFilter(new JWTAuthorizationFilter(authenticationManager())).sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(
            "/**",
            new CorsConfiguration()
                .applyPermitDefaultValues());
        return source;
    }
}
