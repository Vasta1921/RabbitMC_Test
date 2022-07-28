package com.example.rabbit.amqp

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.amqp.core.*
import org.springframework.amqp.core.Queue
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory
import org.springframework.amqp.rabbit.connection.ConnectionFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
import org.springframework.amqp.support.converter.MessageConverter


@Configuration
class RabbitConfig {

    companion object {
        const val EXCHANGE = "test_exchange"
        const val KEY = "rabbit_key"
        const val QUERY = "rabbit_query"
    }

    @Bean
    fun recv(): Queue = QueueBuilder.nonDurable(QUERY).ttl(15000).build()


    private  var host: String = "localhost"

    private  var username: String = "guest"

    private var password: String = "guest"

    private  var receiveTimeout: Number = 15000

    private  var replyTimeout: Number = 15000
    @Bean
    fun exchangeAccount() = DirectExchange(EXCHANGE)

    @Bean
    fun brokerBindingAccount(): Declarables = Declarables(
        recv(),
        exchangeAccount(),
        BindingBuilder.bind(recv()).to(exchangeAccount()).with(KEY)
    )

    @Bean
    @ConfigurationProperties("spring.rabbitmq")
    fun connectionFactory(): ConnectionFactory {
        val factory = CachingConnectionFactory(host)
        factory.username = username
        factory.setPassword(password)
        return factory
    }

    @Bean
    fun rabbitTemplate(connectionFactory: ConnectionFactory): RabbitTemplate {
        val rabbitTemplate = RabbitTemplate(connectionFactory)
        rabbitTemplate.messageConverter = jsonMessageConverter()
        rabbitTemplate.setUserCorrelationId(true)
        rabbitTemplate.setReplyTimeout(replyTimeout.toLong())
        rabbitTemplate.setReceiveTimeout(receiveTimeout.toLong())

        return rabbitTemplate
    }

    @Bean
    fun jsonMessageConverter(): MessageConverter = Jackson2JsonMessageConverter()
}