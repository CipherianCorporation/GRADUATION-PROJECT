package com.edu.graduationproject.config;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import com.edu.graduationproject.security.CustomUserDetailsService;
import com.edu.graduationproject.security.oauth2.CustomOAuth2UserService;
import com.edu.graduationproject.security.oauth2.OAuthLoginSuccessHandler;
import com.edu.graduationproject.service.UserService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        UserService userService;

        @Autowired
        private CustomUserDetailsService service;

        @Autowired
        private CustomOAuth2UserService oauthUserService;
        @Autowired
        private OAuthLoginSuccessHandler oauthLoginSuccessHandler;

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

        @Bean
        public DaoAuthenticationProvider getDaoAuthenticationProvider() {
                DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
                provider.setPasswordEncoder(passwordEncoder());
                provider.setUserDetailsService(service);
                return provider;
        }

        @Bean
        public SessionRegistry sessionRegistry() {
                return new SessionRegistryImpl();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
                auth.authenticationProvider(getDaoAuthenticationProvider());
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
                http.csrf().disable().cors();
                http.headers().frameOptions().sameOrigin().and().authorizeRequests()
                                .antMatchers("/", "/login**").permitAll()
                                .antMatchers(
                                                "/home/**",
                                                "/security/**",
                                                "/rest/products/**",
                                                "/rest/categories/**",
                                                "/rest/upload/**",
                                                "/rest/order/**",
                                                "/rest/orders/download-invoice**",
                                                "/rest/sub-categories/**",
                                                "/rest/voucher/**",
                                                "/rest/send_contact",
                                                "/cart/**",
                                                "/account/signup",
                                                "/account/contact",
                                                "/account/register",
                                                "/account/forgotpassword/**",
                                                "/verify/**",
                                                "/oauth2/**",
                                                "/reset_password/**",
                                                "/product/list",
                                                "/product/detail/**",
                                                "/callback/",
                                                "/webjars/**",
                                                "/error**",
                                                "/assets/**",
                                                "/upload/images/**",
                                                "/file/**/*.*",
                                                "/*.html",
                                                "/favicon.ico",
                                                "/**/*.html",
                                                "/**/*.css",
                                                "/**/*.js")
                                .permitAll()
                                .antMatchers("/admin/products/**",
                                                "/admin/users/**",
                                                "/admin/orders/**",
                                                "/rest/roles",
                                                "/rest/usersrole/**",
                                                "/assets/admin/**",
                                                "/rest/visitors/**",
                                                "/rest/orders/count",
                                                "/rest/orders/revenue",
                                                "/rest/sub-categories/product-sold")
                                .hasAnyRole("ADMIN", "STAFF")
                                .antMatchers("/rest/authorities/**").hasRole("ADMIN")
                                .anyRequest().authenticated(); // permitAll để code, debug dễ, nên để thành
                                                               // authenticated()
                                                               // sau khi xong
                http.formLogin()
                                .loginPage("/security/login/form")
                                .loginProcessingUrl("/security/login")
                                .defaultSuccessUrl("/security/login/success", false)
                                .failureUrl("/security/login/error");
                http.rememberMe()
                                .key(UUID.randomUUID().toString())
                                .tokenValiditySeconds(1209600) // expired after 14 days
                                .userDetailsService(service);
                http.logout()
                                .logoutUrl("/security/logoff")
                                .logoutSuccessUrl("/security/logoff/success")
                                .clearAuthentication(true)
                                .invalidateHttpSession(true)
                                .deleteCookies("JSESSIONID");

                http.exceptionHandling()
                                .accessDeniedPage("/security/unauthorized");

                // để lưu số lượng session (lưu active users)
                http.sessionManagement()
                                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS).maximumSessions(1)
                                .sessionRegistry(sessionRegistry());

                http.oauth2Login()
                                .loginPage("/security/login/form")
                                .defaultSuccessUrl("/oauth2/login/success", true)
                                .failureUrl("/security/login/error")
                                .authorizationEndpoint()
                                .baseUri("/oauth2/authorization")
                                .and()
                                .userInfoEndpoint()
                                .userService(oauthUserService)
                                .and()
                                .successHandler(oauthLoginSuccessHandler);

                http.headers().frameOptions().disable();
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
                web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
        }

}
