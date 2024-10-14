package caju.transaction.exception

import caju.transaction.enum.TransactionStatusEnum.ERROR
import caju.transaction.rest.response.TransactionResponse
import caju.transaction.transaction.logger.loggerError
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @ExceptionHandler(Exception::class)
    fun handleException(exception: Exception) =
        logAndBuildResponse(
            message = exception.message ?: exception.localizedMessage,
            payload = TransactionResponse(ERROR.code)
        )

    private fun logAndBuildResponse(message: String, payload: TransactionResponse) =
        loggerError(logger, message).run { ResponseEntity.status(OK).body(payload) }
}
