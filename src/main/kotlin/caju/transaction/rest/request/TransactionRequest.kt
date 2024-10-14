package caju.transaction.transaction.rest.request

import caju.transaction.constants.Regex.ONLY_NUMBERS
import jakarta.validation.constraints.Pattern

data class transactionRequest(
    val account: String,
    @field:Pattern(regexp= ONLY_NUMBERS)
    val mcc: String,
    val merchant: String,
    val totalAmount: Double
)