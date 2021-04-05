package com.github.adrian678.forum.forumapp.identityandaccess;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

public class JwtTokenFilter extends OncePerRequestFilter {
    String Auth_HEADER = "Authorization";
    String BEARER_PREFIX = "BEARER ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(Auth_HEADER);
        if(null == header || !header.startsWith(BEARER_PREFIX)){
            filterChain.doFilter(request, response);
            return;
        }

        //strip the prefix from the String
        String token = header.replace(BEARER_PREFIX, "");
        Claims claims = null;
        String env_secret = System.getenv().get("forum_secret_key");
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(env_secret));
        try{
            claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token).getBody();
        } catch (JwtException ex) {
            //TODO figure out what to put if this fails. Is there an invalid credentials exception?
        }
        //TODO convert claims to authorities and pass them into an Authentication?

        if(claims.getExpiration().toInstant().isBefore(Instant.now())){
            //TODO throw an error and have user redirect to login?
        } else {
            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    claims.getSubject(), null, ( (List<String>) claims.get("authorities"))
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList()));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

    }
}
