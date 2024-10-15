package caju.transaction.exception

import caju.transaction.constants.Errors.INVALID_PARAMETER
import caju.transaction.enum.TransactionStatusEnum.ERROR
import caju.transaction.rest.response.TransactionResponse
import caju.transaction.transaction.logger.loggerError
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.http.HttpStatus.OK
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
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

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(exception: Exception) =
        (exception as MethodArgumentNotValidException).fieldError.let {
            logAndBuildResponse(
                message = it?.defaultMessage ?: exception.message,
                payload = ErrorResponse(
                    field = it?.field,
                    message = it?.defaultMessage,
                    error = INVALID_PARAMETER
                )
            )
        }

    private fun logAndBuildResponse(message: String, payload: ErrorResponse) =
        loggerError(logger, message).run { ResponseEntity.status(BAD_REQUEST).body(payload) }

    private fun logAndBuildResponse(message: String, payload: TransactionResponse) =
        loggerError(logger, message).run { ResponseEntity.status(OK).body(payload) }
}

data class ErrorResponse(
    val field: String?,
    val message: String?,
    val error: String?
)
