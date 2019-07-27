package com.shoppingmall.config;

import com.shoppingmall.handler.CustomLoginFailureHandler;
import com.shoppingmall.handler.CustomLoginSuccessHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.OAuth2ClientProperties;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.sql.DataSource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Override
    public void configure(WebSecurity web) throws Exception
    {
        web.ignoring().antMatchers("/resources/**");
    }

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
                    .successHandler(successHandler())
                    //.defaultSuccessUrl("/cart", true)
                    .failureHandler(failureHandler())
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
                    .ignoringAntMatchers("/h2-console/**")
                .and()
                    .sessionManagement()
                    .maximumSessions(1)
                    .expiredUrl("/duplicated-login")
                    .sessionRegistry(sessionRegistry());
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new CustomLoginSuccessHandler("/");
    }

    @Bean
    public AuthenticationFailureHandler failureHandler() { return new CustomLoginFailureHandler(); }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }// Register HttpSessionEventPublisher

    @Bean
    public static ServletListenerRegistrationBean httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean(new HttpSessionEventPublisher());
    }
}