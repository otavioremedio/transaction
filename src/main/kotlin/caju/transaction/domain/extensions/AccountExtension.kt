package caju.transaction.domain.extensions

import caju.transaction.domain.Account
import caju.transaction.domain.Transaction
import caju.transaction.enum.TransactionStatusEnum

fun Account.chargeMeal(amount: Double) =
    takeIf { this.mealBalance >= amount }?.copy(mealBalance = mealBalance - amount)

fun Account.chargeFood(amount: Double) =
    takeIf { this.foodBalance >= amount }?.copy(foodBalance = foodBalance - amount)

fun Account.chargeCash(amount: Double) =
    takeIf { this.cashBalance >= amount }?.copy(cashBalance = cashBalance - amount)

fun Account.updateTransaction(transaction: Transaction, transactionStatus: TransactionStatusEnum) =
    this.transactions.add(transaction.copy(transactionStatus = transactionStatus)).let { this }

fun Account.getLasTransactionStatus() = this.transactions.maxBy { it.createdAt }.transactionStatus!!