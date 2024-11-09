package com.kasymzhan.auth.config

import com.kasymzhan.auth.data.UserRoles
import com.kasymzhan.auth.service.AuthUserDetailsService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfig(val userDetailsService: AuthUserDetailsService) {
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun securityFilterChain(http: HttpSecurity): SecurityFilterChain {
        http
            .csrf { it.disable() }
            .authorizeHttpRequests {
                it.requestMatchers("/auth/*").permitAll()
                    .requestMatchers("/action/*").hasAnyRole(UserRoles.USER, UserRoles.ADMIN)
            }
            .formLogin {
                it.usernameParameter("name")
                    .permitAll()
            }
            .logout {
                it.permitAll()
            }
        return http.build()
    }
}