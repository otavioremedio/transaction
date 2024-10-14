package caju.transaction.service

import caju.transaction.domain.Account
import caju.transaction.enum.TransactionTypeEnum
import caju.transaction.enum.TransactionTypeEnum.FOOD
import caju.transaction.enum.TransactionTypeEnum.MEAL
import caju.transaction.exception.BalanceException
import caju.transaction.integration.MerchantClient
import caju.transaction.repository.AccountRepository
import org.springframework.stereotype.Service

@Service
class TransactionService(
    private val merchantClient: MerchantClient,
    private val accountRepository: AccountRepository,
) {
    fun findMerchant(name: String) = merchantClient.getMerchant(name)
    fun findAccount(account: Int) = accountRepository.findById(account).get()

    fun doCharge(account: Account, transactionType: TransactionTypeEnum, amount: Double) =
        account.let {
            when(transactionType) {
                MEAL -> {
                    if (it.mealBalance <= amount) it.copy(mealBalance = it.mealBalance - amount)
                    else chargeCash(it, amount)
                }
                FOOD -> {
                    if (it.foodBalance <= amount) it.copy(foodBalance = it.foodBalance - amount)
                    else chargeCash(it, amount)
                }
                else -> chargeCash(it, amount)
            }
        }

    private fun chargeCash(account: Account, amount: Double) =
        if(account.cashBalance <= amount) account.copy(cashBalance = account.cashBalance - amount)
        else throw BalanceException()
}
