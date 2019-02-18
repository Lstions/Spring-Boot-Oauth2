package com.example.oauth2.config;

import com.example.oauth2.rbac.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;


@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {


    @Configuration
    @Order(1)
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter{

        @Autowired
        private ClientDetailsService clientDetailsService;

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher("/wx/**")
                    .authorizeRequests()
                        .antMatchers("/wx/**").hasAuthority("WX_TOKEN")
                        .and()
                    .httpBasic().realmName("Api")
                       .and()
                    .userDetailsService(new ClientDetailsUserDetailsService(clientDetailsService));
        }

    }

    @Configuration
    public static class FormLoginWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter{

        @Autowired
        private UserManager userDetailsManager;

        @Override
        @Bean
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userDetailsManager);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http
                    .authorizeRequests()
                    .antMatchers("/login","/login.jsp","/oauth/authorize","/oauth/token","/oauth/check_token","/oauth/error","/oauth/idtoken").permitAll()
                    .antMatchers("/user/create").hasRole("ADMIN")
                    //.antMatchers("/hello").hasAuthority("TEST")
                    //.anyRequest().hasRole("STUDENT")
                    .anyRequest().authenticated()
                    .and()
                    .formLogin()
                    .and()
                    .logout();
        }

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//    }
    }

}
