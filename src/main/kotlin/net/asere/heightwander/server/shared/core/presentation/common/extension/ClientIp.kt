package net.asere.heightwander.server.shared.core.presentation.common.extension

import io.ktor.server.application.ApplicationCall
import io.ktor.server.plugins.origin
import java.net.InetAddress

/**
 * Client IP for rate limiting when the app sits behind a reverse proxy.
 *
 * - Peer is a trusted proxy (loopback / private / [TRUSTED_PROXIES]) → use
 *   [origin] (populated by XForwardedHeaders from X-Forwarded-For).
 * - Peer is not trusted → use the TCP peer and ignore forwarded headers
 *   so clients cannot spoof X-Forwarded-For to bypass limits.
 *
 * Optional env `TRUSTED_PROXIES`: comma-separated hosts or IPs of edge proxies
 * (needed when the load balancer peer is a public IP, e.g. Cloudflare).
 */
fun ApplicationCall.clientIpForRateLimit(): String {
    val peer = request.local.remoteHost
    if (!TrustedProxies.isTrusted(peer)) return peer
    val forwarded = request.origin.remoteHost
    return forwarded.takeIf { it.isNotBlank() } ?: peer
}

internal object TrustedProxies {
    private val configured: Set<String> by lazy {
        System
            .getenv("TRUSTED_PROXIES")
            ?.split(',')
            ?.map { it.trim() }
            ?.filter { it.isNotEmpty() }
            ?.toSet()
            .orEmpty()
    }

    fun isTrusted(remoteHost: String): Boolean {
        if (remoteHost.isBlank()) return false
        if (remoteHost in configured) return true
        val address = runCatching { InetAddress.getByName(remoteHost) }.getOrNull() ?: return false
        return address.isLoopbackAddress ||
            address.isAnyLocalAddress ||
            address.isLinkLocalAddress ||
            address.isSiteLocalAddress
    }
}
