package com.example

import config.RiotGlobals
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureHTTP()
    configureMonitoring()
    configureRouting()

    RiotGlobals.apiKey = environment.config.property("ktor.riot.apiKey").getString()
    println("Loaded Riot API Key: ${RiotGlobals.apiKey}")
}
