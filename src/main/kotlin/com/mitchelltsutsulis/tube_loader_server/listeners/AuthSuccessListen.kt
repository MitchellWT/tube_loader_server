package com.mitchelltsutsulis.tube_loader_server.listeners

import com.mitchelltsutsulis.tube_loader_server.BruteForceDetector
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.event.AuthenticationSuccessEvent
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

@Component
class AuthSuccessListen(
    val req: HttpServletRequest,
    val bruteForceDetector: BruteForceDetector
) : ApplicationListener<AuthenticationSuccessEvent> {
    class AuthBlocked : AuthenticationException("Max login attempts reached")

    override fun onApplicationEvent(event: AuthenticationSuccessEvent) {
        val ipAddr = bruteForceDetector.getIpAddr(req)
        if (bruteForceDetector.blocked(ipAddr)) throw AuthBlocked()
        bruteForceDetector.loginSuccess(ipAddr)
    }
}
