package caju.transaction.service

import caju.transaction.domain.Account
import caju.transaction.domain.Transaction
import caju.transaction.domain.extensions.chargeCash
import caju.transaction.domain.extensions.chargeFood
import caju.transaction.domain.extensions.chargeMeal
import caju.transaction.domain.extensions.updateTransaction
import caju.transaction.enum.TransactionStatusEnum.APPROVED
import caju.transaction.enum.TransactionStatusEnum.DENIED
import caju.transaction.enum.TransactionTypeEnum.FOOD
import caju.transaction.enum.TransactionTypeEnum.MEAL
import caju.transaction.integration.MerchantClient
import caju.transaction.repository.AccountRepository
import org.springframework.stereotype.Service

@Service
class TransactionService(
    private val merchantClient: MerchantClient,
    private val accountRepository: AccountRepository,
) {
    fun findMerchant(name: String) = merchantClient.findMerchant(name)
    fun findAccount(account: Int) = accountRepository.findById(account).get()

    fun doCharge(account: Account, transaction: Transaction) =
         account.run {
            when(transaction.transactionType) {
                                MEAL -> this.chargeMeal(transaction.amount) ?: this.chargeCash(transaction.amount)
                                FOOD -> this.chargeFood(transaction.amount) ?: this.chargeCash(transaction.amount)
                                else -> this.chargeCash(transaction.amount)
                        }?.updateTransaction(transaction, APPROVED) ?: this.updateTransaction(transaction, DENIED)
        }

    fun save(account: Account) = accountRepository.save(account)

}
