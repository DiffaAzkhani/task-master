package com.taskmaster.taskmaster.security;

import com.taskmaster.taskmaster.enums.UserRole;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    private final AuthenticationProvider authenticationProvider;

    private final AuthenticationEntryPoint authenticationEntryPoint;

    private final AccessDeniedHandler accessDeniedHandler;

    private static final String[] AUTH_WHITE_LIST = {
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/v2/api-docs/**",
        "/swagger-resources/**"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
            .csrf()
            .disable()
            .authorizeRequests()
                .antMatchers(AUTH_WHITE_LIST).permitAll()
                .antMatchers(HttpMethod.POST,"/api/v1/auth/login").permitAll()
                .antMatchers(HttpMethod.POST,"/api/v1/user/register").permitAll()
                .antMatchers(HttpMethod.GET,"/api/v1/study/{studyCode}").permitAll()
                .antMatchers(HttpMethod.GET,"/api/v1/study/all-study").permitAll()
                // API USER
                .antMatchers(HttpMethod.DELETE,"/api/v1/user").hasRole(UserRole.USER.name())
                .antMatchers(HttpMethod.PATCH,"/api/v1/user/profile/{username}").hasRole(UserRole.USER.name())
                .antMatchers(HttpMethod.GET,"/api/v1/user/my-studies").hasRole(UserRole.USER.name())
                .antMatchers(HttpMethod.GET,"/api/v1/user/profile").hasRole(UserRole.USER.name())
                // API STUDY
                .antMatchers(HttpMethod.PATCH,"/api/v1/study/{studyCode}").hasRole(UserRole.ADMIN.name())
                // API QUESTION AND ANSWER
                .antMatchers(HttpMethod.POST,"/api/v1/qna/add-question").hasRole(UserRole.ADMIN.name())
                .antMatchers(HttpMethod.GET,"/api/v1/qna").hasRole(UserRole.USER.name())
                .antMatchers(HttpMethod.POST,"/api/v1/qna/save-user-answer").hasRole(UserRole.USER.name())
                .antMatchers(HttpMethod.POST,"/api/v1/qna/grade-submission").hasRole(UserRole.USER.name())
                .antMatchers(HttpMethod.GET,"/api/v1/qna/explanation").hasRole(UserRole.USER.name())
                // API ORDER
                .antMatchers(HttpMethod.POST,"/api/v1/order/checkout").hasRole(UserRole.USER.name())
                .antMatchers(HttpMethod.POST,"/api/v1/order/cancel-order").hasRole(UserRole.USER.name())
                .antMatchers(HttpMethod.POST,"/api/v1/order/midtrans-callback").hasRole(UserRole.USER.name())
                .antMatchers(HttpMethod.GET,"/api/v1/order/users").hasRole(UserRole.USER.name())
                .antMatchers(HttpMethod.POST,"/api/v1/order/enroll-free").hasRole(UserRole.USER.name())
                // API CART
                .antMatchers(HttpMethod.POST,"/api/v1/cart/add-cart").hasRole(UserRole.USER.name())
                .antMatchers(HttpMethod.GET,"/api/v1/cart/users").hasRole(UserRole.USER.name())
                .antMatchers(HttpMethod.DELETE,"/api/v1/cart/delete/{username}/{orderCode}").hasRole(UserRole.USER.name())
                // API ADMIN PANEL
                .antMatchers(HttpMethod.DELETE,"/api/v1/admin/studies/delete/{studyCode}").hasRole(UserRole.ADMIN.name())
                .antMatchers(HttpMethod.POST,"/api/v1/admin/studies/add-study").hasRole(UserRole.ADMIN.name())
                .antMatchers(HttpMethod.GET,"/api/v1/admin/all-users").hasRole(UserRole.ADMIN.name())
                .antMatchers(HttpMethod.GET,"/api/v1/admin//users/{username}").hasRole(UserRole.ADMIN.name())
                // API COUPON
                .antMatchers(HttpMethod.POST,"/api/v1/coupon/add-coupon").hasRole(UserRole.ADMIN.name())
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                    .authenticationEntryPoint(authenticationEntryPoint)
                    .and()
                .exceptionHandling()
                    .accessDeniedHandler(accessDeniedHandler)
                .and()
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

}
