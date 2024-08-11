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
                .antMatchers(HttpMethod.POST,"/api/v1/users/register").permitAll()
                .antMatchers(HttpMethod.GET,"/api/v1/studies/{studyId}").permitAll()
                .antMatchers(HttpMethod.POST,"/api/v1/studies").hasRole(UserRole.ADMIN.name())
                .antMatchers(HttpMethod.GET,"/api/v1/studies/{studyId}").hasRole(UserRole.ADMIN.name())
                .antMatchers(HttpMethod.DELETE,"/api/v1/studies/{studyId}").hasRole(UserRole.ADMIN.name())
                .antMatchers(HttpMethod.PATCH,"/api/v1/studies/{studyId}").hasRole(UserRole.ADMIN.name())
                .antMatchers(HttpMethod.GET,"/api/v1/orders/me").hasRole(UserRole.USER.name())
                .antMatchers(HttpMethod.GET,"/api/v1/orders/{userId}").hasRole(UserRole.ADMIN.name())
                .antMatchers(HttpMethod.POST,"/api/v1/orders/**").hasRole(UserRole.USER.name())
                .antMatchers(HttpMethod.POST,"/api/v1/carts/items").hasRole(UserRole.USER.name())
                .antMatchers(HttpMethod.GET,"/api/v1/carts").hasRole(UserRole.USER.name())
                .antMatchers(HttpMethod.DELETE,"/api/v1/carts/items/{cartItemId}").hasRole(UserRole.USER.name())
                .antMatchers(HttpMethod.GET,"/api/v1/qna/answers/study/{studyId}").hasRole(UserRole.USER.name())
                .antMatchers(HttpMethod.GET,"/api/v1/qna/me/questions/{studyId}").hasRole(UserRole.USER.name())
                .antMatchers(HttpMethod.GET,"/api/v1/qna/questions/{studyId}").hasRole(UserRole.ADMIN.name())
                .antMatchers(HttpMethod.POST,"/api/v1/qna/questions").hasRole(UserRole.ADMIN.name())
                .antMatchers(HttpMethod.POST,"/api/v1/qna/studies/{studyId}/grade").hasRole(UserRole.USER.name())
                .antMatchers(HttpMethod.POST,"/api/v1/qna/studies/{studyId}/submission").hasRole(UserRole.USER.name())
                .antMatchers(HttpMethod.DELETE,"/api/v1/users/me").hasRole(UserRole.USER.name())
                .antMatchers(HttpMethod.DELETE,"/api/v1/users").hasRole(UserRole.ADMIN.name())
                .antMatchers(HttpMethod.GET,"/api/v1/users/my-studies").hasRole(UserRole.USER.name())
                .antMatchers(HttpMethod.GET,"/api/v1/users").hasRole(UserRole.ADMIN.name())
                .antMatchers(HttpMethod.PATCH,"/api/v1/users/{userId}").hasRole(UserRole.ADMIN.name())
                .antMatchers(HttpMethod.PATCH,"/api/v1/users/me/profile").hasRole(UserRole.USER.name())
                .antMatchers(HttpMethod.GET,"/api/v1/users/{userId}").hasRole(UserRole.ADMIN.name())
                .antMatchers(HttpMethod.POST,"/api/v1/coupons").hasRole(UserRole.ADMIN.name())
                .anyRequest().authenticated()
                .and()
            .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
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
