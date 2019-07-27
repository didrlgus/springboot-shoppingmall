package com.shoppingmall.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/checkout", "/cart", "/profiles**", "/me/**", "/oauth/me/**", "/question**").hasAnyAuthority("ROLE_USER", "ROLE_SOCIAL")
                .antMatchers("/admin/**").hasAuthority("ROLE_ADMIN")
                .anyRequest().permitAll()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
                .accessDeniedPage("/accessDenied")
                .and()
                .formLogin()
                .loginProcessingUrl("/login")
                .and()
                .oauth2Login()
                //.successHandler(successHandler())
                .defaultSuccessUrl("/oauth/loginSuccess", true)
                .failureUrl("/oauth/loginFailed")
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .and()
                .headers().frameOptions().disable()
                .and()
                .csrf()
                .ignoringAntMatchers("/h2-console/**");
    }
}
