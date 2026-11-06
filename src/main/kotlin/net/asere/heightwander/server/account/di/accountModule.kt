package net.asere.heightwander.server.account.di

import net.asere.heightwander.server.account.data.OAuthDataRepository
import net.asere.heightwander.server.account.data.RecoveryDataRepository
import net.asere.heightwander.server.account.data.SessionDataRepository
import net.asere.heightwander.server.account.data.UserDataRepository
import net.asere.heightwander.server.account.data.VerificationDataRepository
import net.asere.heightwander.server.account.data.datasource.AppleOAuthDataSource
import net.asere.heightwander.server.account.data.datasource.AppleOAuthDataSourceImpl
import net.asere.heightwander.server.account.data.datasource.GoogleOAuthDataSource
import net.asere.heightwander.server.account.data.datasource.GoogleOAuthDataSourceImpl
import net.asere.heightwander.server.account.data.datasource.RecoveryDataSource
import net.asere.heightwander.server.account.data.datasource.UserDataSource
import net.asere.heightwander.server.account.data.datasource.VerificationDataSource
import net.asere.heightwander.server.account.data.datasource.database.RecoveryMongodbDataSource
import net.asere.heightwander.server.account.data.datasource.database.UserMongodbDataSource
import net.asere.heightwander.server.account.data.datasource.database.VerificationMongodbDataSource
import net.asere.heightwander.server.account.data.security.CypherDigestInterface
import net.asere.heightwander.server.account.data.security.JwtInterface
import net.asere.heightwander.server.account.domain.AccountProxy
import net.asere.heightwander.server.account.domain.OAuthRepository
import net.asere.heightwander.server.account.domain.RecoveryRepository
import net.asere.heightwander.server.account.domain.SessionRepository
import net.asere.heightwander.server.account.domain.TokenInterface
import net.asere.heightwander.server.account.domain.UserRepository
import net.asere.heightwander.server.account.domain.VerificationRepository
import net.asere.heightwander.server.account.domain.model.CypherInterface
import net.asere.heightwander.server.account.domain.usecase.AdminOnlyCreateUser
import net.asere.heightwander.server.account.domain.usecase.AdminOnlyListUsers
import net.asere.heightwander.server.account.domain.usecase.AdminUpdateUser
import net.asere.heightwander.server.account.domain.usecase.ChangePassword
import net.asere.heightwander.server.account.domain.usecase.CountUsers
import net.asere.heightwander.server.account.domain.usecase.DecodeToken
import net.asere.heightwander.server.account.domain.usecase.DeleteUser
import net.asere.heightwander.server.account.domain.usecase.DoesUserExist
import net.asere.heightwander.server.account.domain.usecase.GetUserById
import net.asere.heightwander.server.account.domain.usecase.GetUserByName
import net.asere.heightwander.server.account.domain.usecase.GetUsersByIds
import net.asere.heightwander.server.account.domain.usecase.LegacyResetPassword
import net.asere.heightwander.server.account.domain.usecase.LegacySendRecoveryCode
import net.asere.heightwander.server.account.domain.usecase.LoginUser
import net.asere.heightwander.server.account.domain.usecase.LoginWithApple
import net.asere.heightwander.server.account.domain.usecase.LoginWithGoogle
import net.asere.heightwander.server.account.domain.usecase.NotifyUsersLegalUpdate
import net.asere.heightwander.server.account.domain.usecase.RefreshSession
import net.asere.heightwander.server.account.domain.usecase.RegisterUser
import net.asere.heightwander.server.account.domain.usecase.ResendVerificationEmail
import net.asere.heightwander.server.account.domain.usecase.ResetPassword
import net.asere.heightwander.server.account.domain.usecase.SearchUsers
import net.asere.heightwander.server.account.domain.usecase.SendRecoveryCode
import net.asere.heightwander.server.account.domain.usecase.SendVerificationEmail
import net.asere.heightwander.server.account.domain.usecase.UpdateUser
import net.asere.heightwander.server.account.domain.usecase.UpdateUserPassword
import net.asere.heightwander.server.account.domain.usecase.VerifyEmail
import net.asere.heightwander.server.account.presentation.api.AccountController
import net.asere.heightwander.server.account.presentation.api.USER_DATA_PLUGIN_NAME
import net.asere.heightwander.server.account.presentation.api.createUserDataPlugin
import net.asere.heightwander.server.shared.account.domain.AccountService
import net.asere.heightwander.server.shared.core.presentation.api.auth.AuthTokenResolver
import org.koin.core.module.dsl.factoryOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module

val accountModule =
    module {
        factory(named(USER_DATA_PLUGIN_NAME)) { createUserDataPlugin(get()) }
        factoryOf(::AuthTokenResolver)
        factoryOf(::RecoveryMongodbDataSource).bind(RecoveryDataSource::class)
        factoryOf(::VerificationMongodbDataSource).bind(VerificationDataSource::class)
        factoryOf(::UserMongodbDataSource).bind(UserDataSource::class)
        factoryOf(::GoogleOAuthDataSourceImpl).bind(GoogleOAuthDataSource::class)
        factoryOf(::AppleOAuthDataSourceImpl).bind(AppleOAuthDataSource::class)
        factoryOf(::UserDataRepository).bind(UserRepository::class)
        factoryOf(::RecoveryDataRepository).bind(RecoveryRepository::class)
        factoryOf(::VerificationDataRepository).bind(VerificationRepository::class)
        factoryOf(::SessionDataRepository).bind(SessionRepository::class)
        factoryOf(::OAuthDataRepository).bind(OAuthRepository::class)
        factoryOf(::JwtInterface).bind(TokenInterface::class)
        factoryOf(::CypherDigestInterface).bind(CypherInterface::class)
        factoryOf(::DecodeToken)
        factoryOf(::RegisterUser)
        factoryOf(::LoginUser)
        factoryOf(::LoginWithGoogle)
        factoryOf(::LoginWithApple)
        factoryOf(::AdminOnlyListUsers)
        factoryOf(::DeleteUser)
        factoryOf(::AdminUpdateUser)
        factoryOf(::UpdateUser)
        factoryOf(::GetUserById)
        factoryOf(::GetUsersByIds)
        factoryOf(::DoesUserExist)
        factoryOf(::GetUserByName)
        factoryOf(::AdminOnlyCreateUser)
        factoryOf(::SearchUsers)
        factoryOf(::CountUsers)
        factoryOf(::LegacyResetPassword)
        factoryOf(::LegacySendRecoveryCode)
        factoryOf(::SendRecoveryCode)
        factoryOf(::SendVerificationEmail)
        factoryOf(::NotifyUsersLegalUpdate)
        factoryOf(::VerifyEmail)
        factoryOf(::ResendVerificationEmail)
        factoryOf(::UpdateUserPassword)
        factoryOf(::ChangePassword)
        factoryOf(::ResetPassword)
        factoryOf(::RefreshSession)
        factoryOf(::AccountProxy).bind(AccountService::class)
        factoryOf(::AccountController)
    }
