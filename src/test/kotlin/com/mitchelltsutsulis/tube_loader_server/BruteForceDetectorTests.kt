package com.mitchelltsutsulis.tube_loader_server

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class BruteForceDetectorTests(@Autowired val bruteForceDetector: BruteForceDetector) {
    val ipAddr = "192.168.1.20"

    @Test
    fun `test attempt incrementing when login fails`() {
        bruteForceDetector.loginFail(ipAddr)
        assertThat(bruteForceDetector.attemptCache.getValue(ipAddr) == 1).isEqualTo(true)

        bruteForceDetector.loginFail(ipAddr)
        assertThat(bruteForceDetector.attemptCache.getValue(ipAddr) == 2).isEqualTo(true)

        bruteForceDetector.loginFail(ipAddr)
        assertThat(bruteForceDetector.attemptCache.getValue(ipAddr) == 3).isEqualTo(true)

        bruteForceDetector.attemptCache.remove(ipAddr)
    }

    @Test
    fun `test attempt reset when login succeeds and max attempts is not exceeded`() {
        bruteForceDetector.attemptCache[ipAddr] = 5
        bruteForceDetector.loginSuccess(ipAddr)

        assertThat(bruteForceDetector.attemptCache.contains(ipAddr)).isEqualTo(false)

        bruteForceDetector.attemptCache.remove(ipAddr)
    }

    @Test
    fun `test attempt test when login succeeds and max attempts is exceeded`() {
        val attemptCount = bruteForceDetector.securityConfig.passwordAttemptMax + 1

        bruteForceDetector.attemptCache[ipAddr] = attemptCount
        bruteForceDetector.loginSuccess(ipAddr)

        assertThat(bruteForceDetector.attemptCache.getValue(ipAddr)).isEqualTo(attemptCount)

        bruteForceDetector.attemptCache.remove(ipAddr)
    }
}