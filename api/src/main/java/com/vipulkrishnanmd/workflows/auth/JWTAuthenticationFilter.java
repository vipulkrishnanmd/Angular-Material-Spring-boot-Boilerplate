package com.vipulkrishnanmd.workflows.auth;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vipulkrishnanmd.workflows.model.User;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    public static final String JWT_SECRET = "workflow1234workflow1234";
    public static final long EXPIRATION_PERIOD = 24*60*60*1000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_NAME = "Authorization";

    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
       this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try{
            User user =
                new ObjectMapper()
                    .readValue(request.getInputStream(), User.class);

            return authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    user.getUsername(), user.getPassword(), new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain,
        Authentication authResult)
        throws IOException, ServletException {
        String token =
            JWT.create()
                .withSubject(
                    ((org.springframework.security.core.userdetails.User) authResult.getPrincipal()).getUsername())
                .withExpiresAt(
                    new Date(System.currentTimeMillis() + EXPIRATION_PERIOD))
                .sign(Algorithm.HMAC512(JWT_SECRET.getBytes()));
        AuthResponse authResponse = new AuthResponse(((org.springframework.security.core.userdetails.User) authResult.getPrincipal()).getUsername(), token);
        ObjectMapper objectMapper = new ObjectMapper();
        String authReponseJsonString = objectMapper.writeValueAsString(authResponse);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(authReponseJsonString);
        out.flush();
    }
}
