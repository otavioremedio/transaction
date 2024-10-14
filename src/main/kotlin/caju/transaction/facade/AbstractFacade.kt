package caju.transaction.facade

import caju.transaction.constants.Logs.LOG_METHOD
import caju.transaction.transaction.logger.Kv
import caju.transaction.transaction.logger.loggerInfo
import caju.transaction.transaction.logger.toJson
import java.lang.reflect.InvocationTargetException
import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class AbstractFacade {
    private val logger: Logger = LoggerFactory.getLogger(this.javaClass)

    protected fun <R> executeAndLog(func: () -> R): R {
        return try {
            func()
                .also {
                    loggerInfo(
                        logger,
                        it!!.toJson(),
                        Kv(LOG_METHOD, func::class.java.enclosingMethod.name),
                    )
                }
        } catch (ex: InvocationTargetException) {
            throw ex.targetException
        }
    }
}
