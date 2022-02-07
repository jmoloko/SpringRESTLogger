package com.milk.restfilelogger.config;

import com.milk.restfilelogger.entity.Permission;
import com.milk.restfilelogger.security.JwtConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Jack Milk
 */

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtConfigurer jwtConfigurer;

    private static final String USER_ENDPOINT = "/api/v1/user/**";
    private static final String EXECUTIVE_ENDPOINT = "/api/v1/executive/**";
    private static final String LOGIN_ENDPOINT = "/api/v1/auth/**";

    @Autowired
    public SecurityConfig(JwtConfigurer jwtConfigurer) {
        this.jwtConfigurer = jwtConfigurer;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(LOGIN_ENDPOINT).permitAll()
                .antMatchers(USER_ENDPOINT).hasAnyAuthority(Permission.LEVEL_LOW.getPermission(), Permission.LEVEL_MIDDLE.getPermission(), Permission.LEVEL_HIGH.getPermission())
                .antMatchers(EXECUTIVE_ENDPOINT).hasAnyAuthority(Permission.LEVEL_MIDDLE.getPermission(), Permission.LEVEL_HIGH.getPermission())
                .anyRequest().authenticated()
                .and()
                .apply(jwtConfigurer);

    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

}
