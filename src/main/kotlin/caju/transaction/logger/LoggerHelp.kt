package caju.transaction.transaction.logger

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import net.logstash.logback.marker.LogstashMarker
import net.logstash.logback.marker.Markers
import org.slf4j.Logger

data class Kv(val key: String, val value: Any?)

fun loggerInfo(logger: Logger, message: String, vararg kv: Kv) {
    val payloadMarker = turnToMap(kv)
    logger.info(payloadMarker, message)
}

fun loggerError(logger: Logger, message: String, vararg kv: Kv) {
    val payloadMarker = turnToMap(kv)
    logger.error(payloadMarker, message)
}


private fun turnToMap(kv: Array<out Kv>): LogstashMarker {
    val map = mutableMapOf<String, Any?>()

    kv.forEach { item ->
        map[item.key] = item.value ?: ""
    }

    return Markers.appendEntries(map)
}

fun Any.toJson(): String {
    return jacksonObjectMapper()
        .registerModule(JavaTimeModule())
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        .writeValueAsString(this)
}