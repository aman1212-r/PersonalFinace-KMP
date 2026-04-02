package com.example.personalfinance

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform