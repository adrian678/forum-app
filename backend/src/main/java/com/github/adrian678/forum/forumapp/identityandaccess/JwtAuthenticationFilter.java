package com.github.adrian678.forum.forumapp.identityandaccess;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.adrian678.forum.forumapp.domain.user.CreateUserRequestDto;
import io.jsonwebtoken.io.Decoders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager){
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("Authentication attempted");
        try{
            CreateUserRequestDto dto = new ObjectMapper().readValue(request.getInputStream(), CreateUserRequestDto.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword(), new ArrayList<>()));
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        String env_secret = System.getenv().get("forum_secret_key");
        SecretKey key =Keys.hmacShaKeyFor(Decoders.BASE64.decode(env_secret));
        Instant issuedAt = Instant.now();
        String jws = Jwts.builder()
            .setSubject(authResult.getName())
        //TODO add claims
            .claim("authorities", authResult.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
            .setIssuedAt(Date.from(issuedAt))
            .setExpiration(Date.from(issuedAt.plus(1, ChronoUnit.DAYS)))
            .signWith(key)
            .compact();
            //Check if the route is available to users/admin/whoever
        response.getWriter().write(jws);
        response.getWriter().flush();
    }

//    @Override
//    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
//        System.out.println(failed.getCause());
//        System.out.println(failed.getMessage());
//        super.unsuccessfulAuthentication(request, response, failed);
//    }
}
