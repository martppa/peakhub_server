package net.asere.heightwander.server.account.presentation.api

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.forwardedheaders.XForwardedHeaders
import io.ktor.server.plugins.ratelimit.RateLimit
import io.ktor.server.plugins.ratelimit.RateLimitName
import io.ktor.server.plugins.ratelimit.RateLimitProviderConfig
import kotlin.time.Duration.Companion.minutes
import net.asere.heightwander.server.shared.core.presentation.common.extension.clientIpForRateLimit
import net.asere.heightwander.server.shared.core.presentation.common.extension.userId

object RateLimits {
    val Auth = RateLimitName("auth")
    val AuthEmail = RateLimitName("auth-email")
    val AuthPassword = RateLimitName("auth-password")
    val AuthRefresh = RateLimitName("auth-refresh")
    val AccountWrite = RateLimitName("account-write")
    val AccountDestructive = RateLimitName("account-destructive")
}

fun Application.installAuthRateLimit() {
    install(XForwardedHeaders)
    install(RateLimit) {
        fun RateLimitProviderConfig.byIp() {
            requestKey { call -> call.clientIpForRateLimit() }
        }

        fun RateLimitProviderConfig.byUserOrIp() {
            requestKey { call -> call.userId ?: call.clientIpForRateLimit() }
        }

        register(RateLimits.Auth) {
            rateLimiter(limit = 10, refillPeriod = 1.minutes)
            byIp()
        }
        register(RateLimits.AuthEmail) {
            rateLimiter(limit = 5, refillPeriod = 1.minutes)
            byIp()
        }
        register(RateLimits.AuthPassword) {
            rateLimiter(limit = 5, refillPeriod = 1.minutes)
            byUserOrIp()
        }
        register(RateLimits.AuthRefresh) {
            rateLimiter(limit = 30, refillPeriod = 1.minutes)
            byIp()
        }
        register(RateLimits.AccountWrite) {
            rateLimiter(limit = 10, refillPeriod = 1.minutes)
            byUserOrIp()
        }
        register(RateLimits.AccountDestructive) {
            rateLimiter(limit = 3, refillPeriod = 1.minutes)
            byUserOrIp()
        }
    }
}
