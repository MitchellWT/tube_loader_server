package com.mitchelltsutsulis.tube_loader_server.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager

@Configuration
@ConfigurationProperties(prefix = "account")
class SecurityConfig(
    var username: String = "",
    var password: String = ""
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
    fun passwordEncoder() = BCryptPasswordEncoder()
}
