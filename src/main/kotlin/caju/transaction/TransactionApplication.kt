package caju.transaction

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TransactionApplication

fun main(args: Array<String>) {
	runApplication<caju.transaction.TransactionApplication>(*args)
}
