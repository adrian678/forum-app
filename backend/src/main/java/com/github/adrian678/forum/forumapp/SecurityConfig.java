package com.github.adrian678.forum.forumapp;

import com.github.adrian678.forum.forumapp.identityandaccess.JwtAuthenticationFilter;
import com.github.adrian678.forum.forumapp.identityandaccess.JwtTokenFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    String ALL_ROUTES = "/*";


    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
        http
            .authorizeRequests()
                .anyRequest().permitAll()
                .and()
            .cors()
                .and()
            .csrf()
                .disable()
            .httpBasic().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
//                TODO try changing filters to addBefore? add JWTAuth before UsernamePassword filter?
            .addFilter(new JwtAuthenticationFilter(authenticationManager()))
            .addFilterBefore(new JwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
