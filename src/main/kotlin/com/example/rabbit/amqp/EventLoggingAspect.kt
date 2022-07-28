package com.example.rabbit.amqp

import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.slf4j.MDC
import org.springframework.stereotype.Component
import java.util.*

@Aspect
@Component
class EventLoggingAspect {

    @Around("@annotation(org.springframework.amqp.rabbit.annotation.RabbitListener)")
    fun logMethod(joinPoint: ProceedingJoinPoint): Any? {
        try {
            val arg = joinPoint.args.first()
            if (arg != null && arg is ExchangeData<*>) {
                val requestId = arg.requestId ?: UUID.randomUUID().toString()
                MDC.put("_request_id", requestId)
            }
            MDC.put("_path", joinPoint.signature.toShortString())
            val timer = System.currentTimeMillis()
            val result = joinPoint.proceed()
            RabbitListner.logger.info("proceed broker event in ${System.currentTimeMillis() - timer}, request: $arg, response: $result")
            return result
        } finally {
            MDC.clear()
        }
    }
}