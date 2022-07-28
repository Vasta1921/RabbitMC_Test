package com.example.rabbit.service

import com.example.rabbit.amqp.ExchangeData
import com.example.rabbit.amqp.MessageBuilder
import com.example.rabbit.dto.MathEnum
import com.example.rabbit.dto.allSum
import com.example.rabbit.dto.payloadRequest
import org.springframework.stereotype.Service
import java.util.concurrent.Callable
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

@Service
class MathService(private val messageBuilder: MessageBuilder) {

    val timePortalLoad :Long = 5

    private val executor = ThreadPoolExecutor(
        /* corePoolSize = */ 2,
        /* maximumPoolSize = */ 6,
        /* keepAliveTime = */ 10,
        /* unit = */ TimeUnit.SECONDS,
        /* workQueue = */ LinkedBlockingQueue()
    )

    fun getAllSum(request: ExchangeData<payloadRequest>): ExchangeData<allSum?> {
        request.data ?: return messageBuilder.makeResponse(
            request = request, error = "Request dont have data"
        )
        val futuresList = listOf(
            Pair(
                executor.submit(startPartialLoadSum(request.data.numberOne!!, request.data.numberTwo!!)),
                MathEnum.SUM
            ),
            Pair(
                executor.submit(startPartialLoadMinus(request.data.numberOne!!, request.data.numberTwo!!)),
                MathEnum.MINUS
            ),
            Pair(
                executor.submit(startPartialLoadMultiplication(request.data.numberOne!!, request.data.numberTwo!!)),
                MathEnum.MULTIPLICATION
            ),
            Pair(
                executor.submit(startPartialLoadDivision(request.data.numberOne!!, request.data.numberTwo!!)),
                MathEnum.DIVISION
            )
        )

        val resultView = allSum()

        futuresList.forEach() { (future, name) ->
            val result = future.get(timePortalLoad, TimeUnit.SECONDS)
            when (name){
                MathEnum.SUM -> {
                    resultView.sum = result
                }
                MathEnum.MINUS -> {
                    resultView.minus = result
                }
                MathEnum.MULTIPLICATION -> {
                    resultView.multiplication = result
                }
                MathEnum.DIVISION -> {
                    resultView.division = result
                }
            }
        }
        return messageBuilder.makeResponse(
            request = request, data = resultView
        )
    }

    private fun startPartialLoadSum(
        a: Double, b: Double
    ): Callable<Double> = Callable {
        getSumServices(a, b)
    }

    private fun startPartialLoadMinus(
        a: Double, b: Double
    ): Callable<Double> = Callable {
        getMinusServices(a, b)
    }

    private fun startPartialLoadMultiplication(
        a: Double, b: Double
    ): Callable<Double> = Callable {
        getMultiplicationServices(a, b)
    }

    private fun startPartialLoadDivision(
        a: Double, b: Double
    ): Callable<Double> = Callable {
        getDivisionServices(a, b)
    }


    fun getSumServices(a: Double, b: Double): Double? {
        return a + b
    }

    fun getMinusServices(a: Double, b: Double): Double? {
        return a - b
    }

    fun getMultiplicationServices(a: Double, b: Double): Double? {
        return a * b
    }

    fun getDivisionServices(a: Double, b: Double): Double? {
        return a / b
    }
}