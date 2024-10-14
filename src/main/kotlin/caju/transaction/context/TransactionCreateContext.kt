package caju.transaction.context

import caju.transaction.domain.Account
import caju.transaction.enum.TransactionTypeEnum
import caju.transaction.integration.response.MerchantResponse
import caju.transaction.transaction.rest.request.transactionRequest

data class TransactionCreateContext(
    val transactionRequest: transactionRequest,
    val merchant: MerchantResponse? = null,
    val transactionType: TransactionTypeEnum? = null,
    val account: Account? = null
) {
    fun addMerchant(merchant: MerchantResponse) = copy(merchant = merchant)
    fun addTransactionType(transactionTypeEnum: TransactionTypeEnum) = copy(transactionType = transactionTypeEnum)
    fun addAccount(account: Account) = copy(account = account)
}

