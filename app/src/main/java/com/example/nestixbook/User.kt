package com.example.nestixbook

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("ID")
    val id: Int?,
    @SerializedName("First Name")
    val first_name: String?,
    @SerializedName("Last Name")
    val last_name: String?,
    @SerializedName("Email")
    val email: String?,
    @SerializedName("Token")
    val token:String?

)
