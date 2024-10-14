package caju.transaction.component.tests

import caju.transaction.component.config.AbstractComponent
import caju.transaction.component.constants.URLConstants.URL_TRANSACTION
import caju.transaction.component.extensions.GivenByAsn
import caju.transaction.component.extensions.StartMocks
import caju.transaction.component.extensions.assertThatStatusCodeIsEqualTo
import caju.transaction.component.extensions.givenDefault
import caju.transaction.component.wiremock.MerchantMock
import caju.transaction.domain.Account
import caju.transaction.enum.TransactionStatusEnum.APPROVED
import caju.transaction.enum.TransactionTypeEnum.FOOD
import caju.transaction.enum.TransactionTypeEnum.MEAL
import caju.transaction.rest.request.TransactionRequest
import caju.transaction.rest.response.TransactionResponse
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.clearInvocations
import com.nhaarman.mockitokotlin2.doAnswer
import com.nhaarman.mockitokotlin2.inOrder
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import java.util.stream.Stream
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.ArgumentsProvider
import org.junit.jupiter.params.provider.ArgumentsSource
import org.springframework.http.HttpStatus.OK

class CreateTransactionTest : AbstractComponent() {

    @BeforeEach
    fun setup() {
        inOrder = inOrder(transactionService, merchantClient)
        clearInvocations(transactionService, merchantClient)

        transactionCaptor = argumentCaptor()
        accountCaptor = argumentCaptor()
    }

    @ParameterizedTest(name = "[{index}] => ''{0}''")
    @ArgumentsSource(TransactionParameters::class)
    fun `Deve processar uma transação do tipo`(type: String, amount: Double, merchant: String, mcc: String){
        lateinit var account: Account
        lateinit var initialAccount: Account

        doAnswer {
            account = it.callRealMethod() as Account
            initialAccount = account.copy()
            account
        }.`when`(transactionService).findAccount(any())

        StartMocks {
            MerchantMock().configSuccess(name = merchant, mcc = mcc)
        } GivenByAsn {
            givenDefault()
                .body(buildRequest("123", amount, merchant, mcc))
        } When {
            post( URL_TRANSACTION)
        } Then {
            assertThatStatusCodeIsEqualTo(OK)
            inOrder.verify(merchantClient).findMerchant(any())
            inOrder.verify(transactionService).findAccount(any())
            inOrder.verify(transactionService).doCharge(any(), transactionCaptor.capture())
            inOrder.verify(transactionService).save(accountCaptor.capture())
            inOrder.verifyNoMoreInteractions()

            val transaction = transactionCaptor.firstValue
            val accountSaved = accountCaptor.firstValue
            assertThat(transaction.transactionType).isEqualTo(account.transactions.first().transactionType)

            when(transaction.transactionType) {
                MEAL -> {
                    assertThat(accountSaved.mealBalance).isEqualTo(initialAccount.mealBalance.minus(amount))
                    assertThat(accountSaved.foodBalance).isEqualTo(initialAccount.foodBalance)
                    assertThat(accountSaved.cashBalance).isEqualTo(initialAccount.cashBalance)
                }
                FOOD ->  {
                    assertThat(accountSaved.foodBalance).isEqualTo(initialAccount.foodBalance.minus(amount))
                    assertThat(accountSaved.mealBalance).isEqualTo(initialAccount.mealBalance)
                    assertThat(accountSaved.cashBalance).isEqualTo(initialAccount.cashBalance)
                }
                else ->  {
                    assertThat(accountSaved.cashBalance).isEqualTo(initialAccount.cashBalance.minus(amount))
                    assertThat(accountSaved.mealBalance).isEqualTo(initialAccount.mealBalance)
                    assertThat(accountSaved.foodBalance).isEqualTo(initialAccount.foodBalance)
                }
            }
        } Extract {
            with(`as`(TransactionResponse::class.java)) {
                assertThat(code).isEqualTo(APPROVED.code)
            }
        }
    }

    internal class TransactionParameters : ArgumentsProvider {
        override fun provideArguments(context: ExtensionContext): Stream<out Arguments> {
            return Stream.of(
                Arguments.of("CASH", "10.00", "UBER TRIP                   SAO PAULO BR", "5835"),
                Arguments.of("FOOD", "10.00", "UBER EATS                   SAO PAULO BR", "5412"),
                Arguments.of("MEAL", "10.00", "PAG*JoseDaSilva          RIO DE JANEI BR", "5812"),
            )
        }
    }

    private fun buildRequest(account: String, amount: Double, merchant: String, mcc: String) =
        TransactionRequest(
            account = account,
            totalAmount = amount,
            mcc = mcc,
            merchant = merchant
        ).let {
            jacksonObjectMapper().writeValueAsString(it)
        }
}