package com.github.adrian678.forum.forumapp;

import com.github.adrian678.forum.forumapp.identityandaccess.JwtAuthenticationFilter;
import com.github.adrian678.forum.forumapp.identityandaccess.JwtTokenFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    String ALL_ROUTES = "/*";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
        http

                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .mvcMatchers("/sign-up").permitAll()
                .mvcMatchers("/me/**").authenticated()
//                .anyRequest().permitAll()
//                .antMatchers("/sign-up").permitAll()
//                .anyRequest().authenticated()
                .and()
                .anonymous().disable()
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilterAfter(new JwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);


//                .and()
//                .authorizeRequests().antMatchers(ALL_ROUTES).permitAll();

                //TODO remove any authentication policies for first tests



    }
}
