package net.asere.heightwander.server.account.presentation.api

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.routing.*
import net.asere.omni.ktor.respond
import net.asere.heightwander.server.account.presentation.model.AppleLoginRequest
import net.asere.heightwander.server.account.presentation.model.GoogleLoginRequest
import net.asere.heightwander.server.account.presentation.model.LoginRequest
import net.asere.heightwander.server.account.presentation.model.PasswordChangeRequest
import net.asere.heightwander.server.account.presentation.model.RecoveryRequest
import net.asere.heightwander.server.account.presentation.model.RefreshSessionRequestModel
import net.asere.heightwander.server.account.presentation.model.RegistrationRequest
import net.asere.heightwander.server.account.presentation.model.RequestCodeRequest
import net.asere.heightwander.server.account.presentation.model.ResendVerificationRequest
import net.asere.heightwander.server.account.presentation.model.UserModel
import net.asere.heightwander.server.account.presentation.model.VerifyEmailRequest
import net.asere.heightwander.server.main.server.ktor.KtorServer
import net.asere.heightwander.server.shared.account.api.AuthorizationScopes
import net.asere.heightwander.server.shared.core.presentation.common.extension.userId
import org.koin.core.component.inject

fun KtorServer.authRoutes(application: Application): Unit = with(application) {
    val controller: AccountController by inject()

    routing {
        rateLimit(RateLimits.Auth) {
            post<RegistrationRequest>(path = "/api/user/register") {
                call.respond {
                    controller.registerUser(it)
                }
            }

            post<LoginRequest>(path = "/api/user/login") {
                call.respond {
                    controller.loginUser(it)
                }
            }

            post<GoogleLoginRequest>("/api/user/login/google") {
                call.respond {
                    controller.loginGoogle(it)
                }
            }

            post<AppleLoginRequest>("/api/user/login/apple") {
                call.respond {
                    controller.loginApple(it)
                }
            }
        }

        rateLimit(RateLimits.AuthEmail) {
            post<RequestCodeRequest>(path = "/api/user/password/code") {
                call.respond {
                    controller.requestRecoveryCode(it)
                }
            }

            post<RecoveryRequest>(path = "/api/user/password/recover") {
                call.respond {
                    controller.recoverPassword(it)
                }
            }

            post<VerifyEmailRequest>(path = "/api/user/verify") {
                call.respond {
                    controller.verifyEmail(it)
                }
            }

            post<ResendVerificationRequest>(path = "/api/user/verify/resend") {
                call.respond {
                    controller.resendVerificationEmail(it)
                }
            }
        }

        authenticate(AuthorizationScopes.API_LOGGED_SCOPE) {
            rateLimit(RateLimits.AccountDestructive) {
                delete(path = "/api/user") {
                    call.respond {
                        controller.deleteUser(call.userId.orEmpty())
                    }
                }
            }

            rateLimit(RateLimits.AccountWrite) {
                patch<UserModel>(path = "/api/user") {
                    call.respond {
                        controller.updateUser(
                            requestingUserId = call.userId.orEmpty(),
                            user = it,
                        )
                    }
                }
            }

            rateLimit(RateLimits.AuthPassword) {
                post<PasswordChangeRequest>(path = "/api/user/password/change") {
                    call.respond {
                        controller.changePassword(call.userId.orEmpty(), it)
                    }
                }
            }
        }

        rateLimit(RateLimits.AuthRefresh) {
            post<RefreshSessionRequestModel>(path = "/api/user/refresh") {
                call.respond {
                    controller.refreshSession(it.refreshToken.orEmpty())
                }
            }
        }
    }
}
