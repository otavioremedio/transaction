package caju.transaction.transaction.rest

import caju.transaction.constants.Logs.MERCHANT
import caju.transaction.constants.URLConstants.URL_TRANSACTION
import caju.transaction.facade.TransactionCreateFacade
import caju.transaction.rest.response.TransactionResponse
import caju.transaction.transaction.logger.Kv
import caju.transaction.transaction.logger.loggerInfo
import caju.transaction.transaction.logger.toJson
import caju.transaction.transaction.rest.request.transactionRequest
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController


@RestController
@Validated
@RequestMapping(URL_TRANSACTION, produces = [APPLICATION_JSON_VALUE])
class TransactionController(
    private val transactionCreateFacade: TransactionCreateFacade,
) {
    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @PostMapping(consumes = [APPLICATION_JSON_VALUE])
    @ResponseStatus(OK)
    fun create(
        @RequestBody
        @Valid
        transactionRequest: transactionRequest,
        request: HttpServletRequest
    ) : TransactionResponse {

        loggerInfo(logger, transactionRequest.toJson(), Kv(MERCHANT, transactionRequest.merchant))

        return transactionCreateFacade.create(transactionRequest)
            .also {
                loggerInfo(logger, it.toJson(), Kv(MERCHANT, transactionRequest.merchant))
            }
    }
}
