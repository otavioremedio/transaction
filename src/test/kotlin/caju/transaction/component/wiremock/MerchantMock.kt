package caju.transaction.component.wiremock

import caju.transaction.component.constants.URLConstants.URL_MERCHANT
import caju.transaction.integration.response.MerchantResponse
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.tomakehurst.wiremock.client.WireMock.aResponse
import com.github.tomakehurst.wiremock.client.WireMock.equalTo
import com.github.tomakehurst.wiremock.client.WireMock.get
import com.github.tomakehurst.wiremock.client.WireMock.stubFor
import com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo
import com.github.tomakehurst.wiremock.http.HttpHeader
import com.github.tomakehurst.wiremock.http.HttpHeaders
import org.springframework.http.HttpHeaders.CONTENT_TYPE
import org.springframework.http.HttpStatus.OK
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.web.util.UriUtils

class MerchantMock {

    fun configSuccess(
        responseHttpHeaders: List<HttpHeader> = listOf(HttpHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)),
        name: String = "UBER TRIP                   SAO PAULO BR",
        mcc: String = "5835"
    ) {
        stubFor(
            get(urlEqualTo(URL_MERCHANT.format(UriUtils.encode(name, "UTF-8"))))
                .withQueryParam("name", equalTo(name))
                .willReturn(
                    aResponse()
                        .withStatus(OK.value())
                        .withHeaders(HttpHeaders(responseHttpHeaders))
                        .withBody(buildBody(name, mcc))
                )
        )
    }

    private fun buildBody(name: String, mcc: String) =
        MerchantResponse(
            id = 1,
            name = name,
            mcc = mcc
        ).let {
            jacksonObjectMapper().writeValueAsString(it)
        }
}