package com.example.nestixbook

data class GoogleBookResponse (
    val items: List<Google>?
)

data class Google (
    val id:String?,
    val volumeInfo:InfoBook?
)

data class InfoBook(
    val title:String?,
    val authors : List<String>?,
    val publishedDate : String?,
    val description: String?,
    val categories: List<String>?,
    val imageLinks: ImageLink?
)

data class ImageLink(
    val thumbnail:String?
)
