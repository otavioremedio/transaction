package caju.transaction.enum

enum class TransactionTypeEnum(val mccList: List<String>? = null) {
    FOOD(listOf("5411","5412")),
    MEAL(listOf("5811","5812")),
    CASH(null);

    companion object {
        fun getTransactionType(mcc: String) =
            entries.firstOrNull{ it.mccList?.contains(mcc) == true } ?: CASH
    }
}

