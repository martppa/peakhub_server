package net.asere.heightwander.server.account.presentation.api

import net.asere.omni.ktor.ResponseContainerHost
import net.asere.omni.ktor.responseContainer
import net.asere.omni.ktor.responseIntent
import net.asere.heightwander.server.account.domain.extension.isValidEmail
import net.asere.heightwander.server.account.domain.extension.isValidPassword
import net.asere.heightwander.server.account.domain.extension.isValidUsername
import net.asere.heightwander.server.account.domain.model.mappable.AccessDisabledError
import net.asere.heightwander.server.account.domain.model.mappable.AccountMappableAggregate
import net.asere.heightwander.server.account.domain.model.mappable.EmailNotVerifiedError
import net.asere.heightwander.server.account.domain.model.mappable.InvalidCredentialsError
import net.asere.heightwander.server.account.domain.model.mappable.InvalidEmailError
import net.asere.heightwander.server.account.domain.model.mappable.InvalidPasswordError
import net.asere.heightwander.server.account.domain.model.mappable.InvalidRecoveryCodeError
import net.asere.heightwander.server.account.domain.model.mappable.InvalidTokenError
import net.asere.heightwander.server.account.domain.model.mappable.InvalidUsernameError
import net.asere.heightwander.server.account.domain.model.mappable.InvalidVerificationCodeError
import net.asere.heightwander.server.account.domain.model.mappable.NeedsTermsConfirmationError
import net.asere.heightwander.server.account.domain.model.mappable.SocialLoginError
import net.asere.heightwander.server.account.domain.model.mappable.Success
import net.asere.heightwander.server.account.domain.model.mappable.UserAlreadyExistError
import net.asere.heightwander.server.account.domain.model.mappable.UserDoesNotExistError
import net.asere.heightwander.server.account.domain.model.mappable.WrongSourceError
import net.asere.heightwander.server.account.domain.usecase.AdminOnlyCreateUser
import net.asere.heightwander.server.account.domain.usecase.AdminUpdateUser
import net.asere.heightwander.server.account.domain.usecase.ChangePassword
import net.asere.heightwander.server.account.domain.usecase.DeleteUser
import net.asere.heightwander.server.account.domain.usecase.GetUserById
import net.asere.heightwander.server.account.domain.usecase.LegacyResetPassword
import net.asere.heightwander.server.account.domain.usecase.LegacySendRecoveryCode
import net.asere.heightwander.server.account.domain.usecase.LoginUser
import net.asere.heightwander.server.account.domain.usecase.LoginWithApple
import net.asere.heightwander.server.account.domain.usecase.LoginWithGoogle
import net.asere.heightwander.server.account.domain.usecase.RefreshSession
import net.asere.heightwander.server.account.domain.usecase.RegisterUser
import net.asere.heightwander.server.account.domain.usecase.ResendVerificationEmail
import net.asere.heightwander.server.account.domain.usecase.UpdateUser
import net.asere.heightwander.server.account.domain.usecase.VerifyEmail
import net.asere.heightwander.server.account.presentation.model.AppleLoginRequest
import net.asere.heightwander.server.account.presentation.model.GoogleLoginRequest
import net.asere.heightwander.server.account.presentation.model.LoginRequest
import net.asere.heightwander.server.account.presentation.model.PasswordChangeRequest
import net.asere.heightwander.server.account.presentation.model.RecoveryRequest
import net.asere.heightwander.server.account.presentation.model.RegistrationRequest
import net.asere.heightwander.server.account.presentation.model.RequestCodeRequest
import net.asere.heightwander.server.account.presentation.model.ResendVerificationRequest
import net.asere.heightwander.server.account.presentation.model.UserModel
import net.asere.heightwander.server.account.presentation.model.VerifyEmailRequest
import net.asere.heightwander.server.account.presentation.model.asDomain
import net.asere.heightwander.server.account.presentation.model.isEmpty
import net.asere.heightwander.server.account.presentation.model.update
import net.asere.heightwander.server.shared.core.domain.environment.Environment
import net.asere.heightwander.server.shared.core.presentation.api.error.ExceptionHandler
import net.asere.heightwander.server.shared.core.presentation.api.error.asCoroutineHandler

class AccountController(
    private val registerUserCase: RegisterUser,
    private val loginUserCase: LoginUser,
    private val loginWithGoogle: LoginWithGoogle,
    private val loginWithApple: LoginWithApple,
    private val deleteUserCase: DeleteUser,
    private val adminUpdateUserCase: AdminUpdateUser,
    private val updateUserCase: UpdateUser,
    private val getUserByIdCase: GetUserById,
    private val adminOnlyCreateUserCase: AdminOnlyCreateUser,
    private val legacySendRecoveryCodeCase: LegacySendRecoveryCode,
    private val resetPasswordCase: LegacyResetPassword,
    private val changePasswordCase: ChangePassword,
    private val refreshSessionCase: RefreshSession,
    private val verifyEmailCase: VerifyEmail,
    private val resendVerificationEmailCase: ResendVerificationEmail,
    private val environment: Environment,
    exceptionHandler: ExceptionHandler,
) : ResponseContainerHost<AccountMappableAggregate, AccountResponse> {
    override val container =
        responseContainer(
            responseMapper = AccountResponseMapper(),
            exceptionMapper = AccountExceptionMapper(),
            coroutineExceptionHandler = exceptionHandler.asCoroutineHandler(),
        )

    fun registerUser(request: RegistrationRequest) = responseIntent {
        if (!environment.isPublicAccessEnabled()) return@responseIntent AccessDisabledError
        if (!request.name.isValidUsername()) return@responseIntent InvalidUsernameError
        if (!request.email.isValidEmail()) return@responseIntent InvalidEmailError
        if (!request.password.isValidPassword()) return@responseIntent InvalidPasswordError

        val result =
            registerUserCase(
                username = request.name,
                email = request.email,
                password = request.password,
                isCoach = request.isCoach,
            )

        when (result) {
            RegisterUser.Result.InvalidEmail -> InvalidEmailError
            RegisterUser.Result.InvalidPassword -> InvalidPasswordError
            RegisterUser.Result.InvalidUsername -> InvalidUsernameError
            RegisterUser.Result.PendingVerification -> Success
            // Kept for sealed exhaustiveness; register no longer returns this (anti-enumeration).
            RegisterUser.Result.UserAlreadyExist -> Success
            RegisterUser.Result.AccessDisabled -> AccessDisabledError
        }
    }

    fun createUser(user: UserModel) = responseIntent {
        if (!user.name.isValidUsername()) return@responseIntent InvalidUsernameError
        if (!user.email.isValidEmail()) return@responseIntent InvalidEmailError
        if (!user.password.isValidPassword()) return@responseIntent InvalidPasswordError

        adminOnlyCreateUserCase(user.asDomain())

        Success
    }

    fun loginUser(request: LoginRequest) = responseIntent {
        val result =
            loginUserCase(
                reference = request.email,
                password = request.password,
            )
        when (result) {
            LoginUser.Result.InvalidCredentials -> InvalidCredentialsError
            LoginUser.Result.SocialLoginError -> SocialLoginError
            LoginUser.Result.EmailNotVerified -> EmailNotVerifiedError
            LoginUser.Result.AccessDisabled -> AccessDisabledError
            is LoginUser.Result.Success -> result.session
        }
    }

    fun loginGoogle(request: GoogleLoginRequest) = responseIntent {
        when (
            val result =
                loginWithGoogle(
                    idToken = request.idToken,
                    acceptedTerms = request.acceptedTerms,
                )
        ) {
            LoginWithGoogle.Result.InvalidToken -> InvalidTokenError
            LoginWithGoogle.Result.WrongSource -> WrongSourceError
            LoginWithGoogle.Result.EmailNotVerified -> EmailNotVerifiedError
            LoginWithGoogle.Result.AccessDisabled -> AccessDisabledError
            LoginWithGoogle.Result.NeedsTermsConfirmation -> NeedsTermsConfirmationError
            is LoginWithGoogle.Result.Success -> result.session
        }
    }

    fun loginApple(request: AppleLoginRequest) = responseIntent {
        when (
            val result =
                loginWithApple(
                    idToken = request.idToken,
                    acceptedTerms = request.acceptedTerms,
                )
        ) {
            LoginWithApple.Result.InvalidToken -> InvalidTokenError
            LoginWithApple.Result.WrongSource -> WrongSourceError
            LoginWithApple.Result.AccessDisabled -> AccessDisabledError
            LoginWithApple.Result.NeedsTermsConfirmation -> NeedsTermsConfirmationError
            is LoginWithApple.Result.Success -> result.session
        }
    }

    fun deleteUser(userId: String) = responseIntent {
        deleteUserCase(userId)
        Success
    }

    /**
     * WARNING!! Only for administrator
     */
    fun updateUser(user: UserModel) = responseIntent {
        when (adminUpdateUserCase(user.asDomain())) {
            AdminUpdateUser.Result.Success -> Success
            AdminUpdateUser.Result.NotFound -> UserDoesNotExistError
            AdminUpdateUser.Result.AlreadyExists -> UserAlreadyExistError
            AdminUpdateUser.Result.InvalidUsername -> InvalidUsernameError
            AdminUpdateUser.Result.InvalidEmail -> InvalidEmailError
            AdminUpdateUser.Result.InvalidPassword -> InvalidPasswordError
        }
    }

    fun updateUser(
        requestingUserId: String,
        user: UserModel,
    ) = responseIntent {
        if (!user.name.isValidUsername()) return@responseIntent InvalidUsernameError
        if (!user.email.isValidEmail()) return@responseIntent InvalidEmailError
        val storedUser = getUserByIdCase(requestingUserId).update(user)
        updateUserCase(storedUser)
        Success
    }

    fun requestRecoveryCode(request: RequestCodeRequest) = responseIntent {
        if (!request.email.isValidEmail()) return@responseIntent InvalidEmailError
        legacySendRecoveryCodeCase(request.email!!)
        Success
    }

    fun recoverPassword(recovery: RecoveryRequest) = responseIntent {
        if (!recovery.password.isValidPassword()) return@responseIntent InvalidPasswordError
        if (recovery.code.isBlank()) return@responseIntent InvalidRecoveryCodeError
        resetPasswordCase(recovery.code, recovery.password)
        Success
    }

    fun changePassword(
        userId: String,
        data: PasswordChangeRequest,
    ) = responseIntent {
        if (data.isEmpty()) return@responseIntent InvalidCredentialsError
        if (!data.newPassword.isValidPassword()) return@responseIntent InvalidPasswordError
        changePasswordCase(
            userId = userId,
            currentPassword = data.currentPassword!!,
            newPassword = data.newPassword!!,
        )
        Success
    }

    fun refreshSession(refreshToken: String) = responseIntent {
        refreshSessionCase(refreshToken = refreshToken)
    }

    fun verifyEmail(request: VerifyEmailRequest) = responseIntent {
        when (verifyEmailCase(request.code)) {
            VerifyEmail.Result.Success -> Success
            VerifyEmail.Result.InvalidVerificationCode -> InvalidVerificationCodeError
        }
    }

    fun resendVerificationEmail(request: ResendVerificationRequest) = responseIntent {
        when (resendVerificationEmailCase(request.email)) {
            ResendVerificationEmail.Result.Success -> Success
            ResendVerificationEmail.Result.InvalidEmail -> InvalidEmailError
        }
    }
}
