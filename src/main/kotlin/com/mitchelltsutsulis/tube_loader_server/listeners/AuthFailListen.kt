package com.mitchelltsutsulis.tube_loader_server.listeners

import com.mitchelltsutsulis.tube_loader_server.BruteForceDetector
import org.springframework.context.ApplicationListener
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest

@Component
class AuthFailListen(
    val req: HttpServletRequest,
    val bruteForceDetector: BruteForceDetector
) : ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    override fun onApplicationEvent(event: AuthenticationFailureBadCredentialsEvent) {
        val ipAddr = bruteForceDetector.getIpAddr(req)
        bruteForceDetector.loginFail(ipAddr)
    }
}
