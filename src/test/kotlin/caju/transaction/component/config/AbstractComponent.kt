package caju.transaction.component.config

import caju.transaction.TransactionApplication
import caju.transaction.domain.Account
import caju.transaction.domain.Transaction
import caju.transaction.integration.MerchantClient
import caju.transaction.service.TransactionService
import com.github.tomakehurst.wiremock.client.WireMock
import com.nhaarman.mockitokotlin2.KArgumentCaptor
import io.restassured.RestAssured
import io.restassured.config.EncoderConfig.encoderConfig
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import org.apache.commons.codec.CharEncoding.UTF_8
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS
import org.mockito.InOrder
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.jdbc.Sql
import org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD
import org.springframework.test.context.jdbc.SqlGroup


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = RANDOM_PORT, classes = [TransactionApplication::class])
@TestInstance(PER_CLASS)
@AutoConfigureWireMock(port = 0)
@SqlGroup(
    Sql("/import_account.sql"),
    Sql(value = ["/clean_up.sql"], executionPhase = AFTER_TEST_METHOD)
)
abstract class AbstractComponent {

    lateinit var inOrder: InOrder

    @SpyBean
    lateinit var transactionService: TransactionService

    @SpyBean
    lateinit var merchantClient: MerchantClient

    @LocalServerPort
    val serverPort: Int = 0

    lateinit var accountCaptor: KArgumentCaptor<Account>
    lateinit var transactionCaptor: KArgumentCaptor<Transaction>

    @BeforeAll
    fun beforeAll() {
        RestAssured.baseURI = "http://localhost"
        RestAssured.port = serverPort
        RestAssured.config = RestAssured.config().encoderConfig(encoderConfig().defaultContentCharset(UTF_8))
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails()
        RestAssured.filters(RequestLoggingFilter(), ResponseLoggingFilter())
    }

    @AfterEach
    fun afterEach() {
        WireMock.reset()
    }
}