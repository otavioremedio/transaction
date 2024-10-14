package caju.transaction.integration

import caju.transaction.constants.URLConstants.URL_MERCHANT
import caju.transaction.integration.response.MerchantResponse
import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@FeignClient(
    value = "merchantClient",
    url = "\${external.merchant.host}"
)
interface MerchantClient {

    @GetMapping(value = [URL_MERCHANT])
    fun findMerchant (
        @RequestParam("name") name: String,
    ) : MerchantResponse
}
