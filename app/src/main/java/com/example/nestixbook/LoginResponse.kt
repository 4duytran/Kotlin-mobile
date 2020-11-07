package com.example.nestixbook

data class LoginResponse(
    val error : Boolean?,
    val message: String?,
    val user: User? = null
)
