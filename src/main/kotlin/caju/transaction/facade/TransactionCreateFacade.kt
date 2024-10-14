package caju.transaction.facade

import caju.transaction.context.TransactionCreateContext
import caju.transaction.enum.TransactionStatusEnum.APPROVED
import caju.transaction.enum.TransactionTypeEnum
import caju.transaction.rest.response.TransactionResponse
import caju.transaction.service.TransactionService
import caju.transaction.transaction.rest.request.transactionRequest
import org.springframework.stereotype.Service

@Service
class TransactionCreateFacade(
        private val transactionService: TransactionService,
) : AbstractFacade() {

    fun create(transactionRequest: transactionRequest
    ): TransactionResponse {
        return TransactionCreateContext(transactionRequest = transactionRequest)
            .let {::findMerchant}
            .let {::findAccount}
            .let {::findTransactionType}
            .let {::doCharge}
            .let { TransactionResponse(APPROVED.code)}

    }

    private fun findMerchant(context: TransactionCreateContext) =
        executeAndLog {
            transactionService
                .findMerchant(context.transactionRequest.merchant)
                .let(context::addMerchant)
        }

    private fun findAccount(context: TransactionCreateContext) =
        executeAndLog {
            transactionService
                .findAccount(context.transactionRequest.account.toInt())
                .let(context::addAccount)
        }

    private fun findTransactionType(context: TransactionCreateContext) =
        executeAndLog {
            TransactionTypeEnum
                .getTransactionType(context.merchant!!.mcc)
                .let(context::addTransactionType)
        }

    private fun doCharge(context: TransactionCreateContext) =
        executeAndLog {
            transactionService
                .doCharge(context.account!!, context.transactionType!!, context.transactionRequest.totalAmount)
                .let(context::addAccount)
        }

}
