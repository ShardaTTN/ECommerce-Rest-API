package com.tothenew.sharda.Security;

import com.tothenew.sharda.Service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    AuthEntryPointJwt unauthorizedHandler;
    @Autowired
    CustomLoginFailureHandler loginFailureHandler;



    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and().authorizeRequests()
                        .antMatchers(HttpMethod.GET, "/api/admin/**").hasRole("ADMIN")
                        .antMatchers(HttpMethod.PATCH, "/api/admin/**").hasRole("ADMIN")
                        .antMatchers(HttpMethod.GET, "/api/customer/**").hasRole("CUSTOMER")
                        .antMatchers(HttpMethod.PUT, "/api/customer/**").hasRole("CUSTOMER")
                        .antMatchers(HttpMethod.POST, "/api/customer/**").hasRole("CUSTOMER")
                        .antMatchers(HttpMethod.DELETE, "/api/customer/**").hasRole("CUSTOMER")
                        .antMatchers(HttpMethod.GET, "/api/seller/**").hasRole("SELLER")
                        .antMatchers(HttpMethod.PUT, "/api/seller/**").hasRole("SELLER")
                        .antMatchers(HttpMethod.POST, "/api/category/**").hasRole("ADMIN")
                        .antMatchers(HttpMethod.PUT, "/api/category/**").hasRole("ADMIN")
                        .antMatchers(HttpMethod.GET, "/api/category/**").hasAnyRole("ADMIN", "SELLER", "CUSTOMER")
                        .antMatchers(HttpMethod.POST, "/api/product/**").hasRole("SELLER")
                        .antMatchers(HttpMethod.DELETE, "/api/product/**").hasRole("SELLER")
                        .antMatchers(HttpMethod.PUT, "/api/product/update-product").hasRole("SELLER")
                        .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/api/logout"))
                        .logoutSuccessUrl("/api/auth/home")
                                .invalidateHttpSession(true)
                                        .deleteCookies("JSESSIONID");
        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}