package net.asere.heightwander.server.account.domain.model.mappable

import net.asere.heightwander.server.shared.core.domain.model.error.GenericError

sealed class AccountError :
    GenericError(),
    AccountMappableAggregate
