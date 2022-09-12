package com.mitchelltsutsulis.tube_loader_server.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain

@Configuration
@ConfigurationProperties(prefix = "security")
class SecurityConfig(
    var username: String = "username",
    var password: String = "password",
    var passwordAttemptMax: Int = 10
) {
    @Bean
    fun userService(passwordEncoder: BCryptPasswordEncoder): UserDetailsService {
        val manager = InMemoryUserDetailsManager()
        manager.createUser(
            User.withUsername(username)
                .password(passwordEncoder.encode(password))
                .roles("USER")
                .build()
        )
        return manager
    }

    @Bean
    fun filter(http: HttpSecurity): SecurityFilterChain {
        http.authorizeRequests()
            .anyRequest().authenticated()
            .and().csrf().disable()
            .httpBasic()
        return http.build()
    }

    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()
}
