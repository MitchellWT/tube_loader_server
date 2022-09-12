package com.mitchelltsutsulis.tube_loader_server

import com.mitchelltsutsulis.tube_loader_server.config.SecurityConfig
import org.springframework.stereotype.Service
import java.util.concurrent.ConcurrentHashMap
import javax.servlet.http.HttpServletRequest

@Service
class BruteForceDetector(val securityConfig: SecurityConfig) {
    val attemptCache = ConcurrentHashMap<String, Int>()

    fun loginSuccess(ipAddr: String) {
        if (blocked(ipAddr)) return
        attemptCache.remove(ipAddr)
    }

    fun loginFail(ipAddr: String) {
        attemptCache.putIfAbsent(ipAddr, 0)
        attemptCache[ipAddr] = attemptCache.getValue(ipAddr) + 1
    }

    fun getIpAddr(req: HttpServletRequest): String {
        val proxyHeader = req.getHeader("X-Forwarded-For") ?: return req.remoteAddr
        return proxyHeader.split(",")[0]
    }

    fun blocked(ipAddr: String): Boolean {
        val attemptCount = attemptCache[ipAddr] ?: return false
        return attemptCount >= securityConfig.passwordAttemptMax
    }
}
