package caju.transaction.context

import caju.transaction.domain.Account
import caju.transaction.domain.Transaction
import caju.transaction.enum.TransactionTypeEnum
import caju.transaction.integration.response.MerchantResponse
import caju.transaction.rest.request.TransactionRequest

data class TransactionCreateContext(
    val transactionRequest: TransactionRequest,
    val merchant: MerchantResponse? = null,
    val transactionType: TransactionTypeEnum? = null,
    val account: Account? = null,
    val transaction: Transaction? = null
) {
    fun addMerchant(merchant: MerchantResponse) = copy(merchant = merchant)
    fun addTransactionType(transactionTypeEnum: TransactionTypeEnum) = copy(transactionType = transactionTypeEnum)
    fun addAccount(account: Account) = copy(account = account)
    fun addTransaction(transaction: Transaction) = copy(transaction = transaction)
}

