package caju.transaction.integration

import caju.transaction.integration.response.MerchantResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.cloud.openfeign.FeignClient

@FeignClient(
    value = "merchantClient",
    url = "\${external.merchant.host}"
)
interface MerchantClient {

    @GetMapping(value = ["/merchants"])
    fun getMerchant (
        @RequestParam("name") name: String,
    ) : MerchantResponse
}
