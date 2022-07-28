package com.example.rabbit.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/re")
class RestTestController {

    @GetMapping("/test")
    fun test() : String {
        return "Test"
    }

}