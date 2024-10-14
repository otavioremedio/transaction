package caju.transaction.rest.request

import caju.transaction.constants.Regex.ONLY_NUMBERS
import jakarta.validation.constraints.Pattern

data class TransactionRequest(
    val account: String,
    @field:Pattern(regexp= ONLY_NUMBERS)
    val mcc: String,
    val merchant: String,
    val totalAmount: Double
)