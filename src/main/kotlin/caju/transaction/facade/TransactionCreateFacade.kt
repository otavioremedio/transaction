package caju.transaction.facade

import caju.transaction.context.TransactionCreateContext
import caju.transaction.enum.TransactionTypeEnum
import caju.transaction.mapper.TransactionMapper
import caju.transaction.rest.request.TransactionRequest
import caju.transaction.rest.response.TransactionResponse
import caju.transaction.service.TransactionService
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class TransactionCreateFacade(private val transactionService: TransactionService) : AbstractFacade() {

    @Transactional
    fun create(
        transactionRequest: TransactionRequest
    ): TransactionResponse {
        return TransactionCreateContext(transactionRequest = transactionRequest)
            .let(::findMerchant)
            .let(::findAccount)
            .let(::findTransactionType)
            .let(::createTransaction)
            .let(::doCharge)
            .let(::saveAccount)
            .let{ TransactionMapper.toResponse(it.account!!) }

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

    private fun createTransaction(context: TransactionCreateContext) =
        TransactionMapper.toEntity(
            context.transactionRequest.totalAmount,
            context.transactionType!!,
            context.account!!
        ).let(context::addTransaction)

    private fun doCharge(context: TransactionCreateContext) =
        executeAndLog {
            transactionService
                .doCharge(context.account!!, context.transaction!!)
                .let(context::addAccount)
        }

    private fun saveAccount(context: TransactionCreateContext) =
        executeAndLog {
            transactionService
                .save(context.account!!)
                .let(context::addAccount)
        }

}