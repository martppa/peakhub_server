package net.asere.heightwander.server.account.presentation.api

import net.asere.omni.ktor.Response
import net.asere.heightwander.server.account.domain.model.mappable.AccountMappableAggregate
import net.asere.heightwander.server.account.presentation.api.model.toResponse
import net.asere.heightwander.server.shared.core.presentation.api.ResponseMapperDecorator

class AccountResponseMapper : ResponseMapperDecorator<AccountMappableAggregate, AccountResponse>() {
    override fun valueOf(input: AccountMappableAggregate): Response<AccountResponse> = input.toResponse()
}
