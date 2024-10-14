package caju.transaction.mapper

import caju.transaction.domain.Account
import caju.transaction.domain.Transaction
import caju.transaction.domain.extensions.getLasTransactionStatus
import caju.transaction.enum.TransactionTypeEnum
import caju.transaction.rest.response.TransactionResponse

object TransactionMapper {

    fun toEntity(
        amount: Double,
        transactionType: TransactionTypeEnum,
        account: Account
    ) = Transaction(
        amount = amount,
        transactionType = transactionType,
        account = account
    )

    fun toResponse(
        account: Account
    ) = TransactionResponse(
        code = account.getLasTransactionStatus().code
    )
}
