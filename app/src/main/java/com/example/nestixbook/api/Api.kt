package com.example.nestixbook.api

import com.example.nestixbook.*
import retrofit2.Call
import retrofit2.http.*

// Get all reponses from API
interface Api {

    /**
     * Reponse from login
     */
    @FormUrlEncoded
    @POST("userlogin")
    fun userLogin(

        @Field("email") email:String,
        @Field("password") password:String
    ):Call<LoginResponse>

    /**
     * Reponse from Register
     */
    @FormUrlEncoded
    // Method POST
    @POST("userregister")
    fun register(

        @Field("first_name") first_name:String,
        @Field("last_name") last_name:String,
        @Field("email") email:String,
        @Field("password") password:String
    ):Call<RegisterResponse>


    @FormUrlEncoded
    @POST("userlogin")
    fun checkLogin(
        @Field("token") token:String
    ):Call<LoginResponse>

    /**
     * Get list of book
     */
    @FormUrlEncoded
    // Method POST
    @POST("media/book")
    fun bookList(
        @Field("token") token:String?
    ):Call<List<Book>> // get response list of book class

    @FormUrlEncoded
    @POST("collection")
    fun collectionList(@Field("token") token:String?):Call<List<BookCollection>>

    @FormUrlEncoded
    @POST("collection")
    fun bookListFromCollection(
        @Field("token") token:String?,
        @Field("collection_id") collectionId:Int?
    ):Call<List<Book>>

    @FormUrlEncoded
    @POST("collection")
    fun addBookCollection(
        @Field("token") token:String?,
        @Field("collection_id") collectionId:Int?,
        @Field("book_id") bookId:Int?
    ):Call<CollectionResponse>

    @FormUrlEncoded
    @POST("collection")
    fun deldBookCollection(
        @Field("token") token:String?,
        @Field("collection_id") collectionId:Int?,
        @Field("book_id") bookId:Int?,
        @Field("delete") delete:Boolean?
    ):Call<CollectionResponse>

    @FormUrlEncoded
    @POST("collection")
    fun createNewCollection(
        @Field("token") token:String?,
        @Field("collection_name") collectionName:String?,
        @Field("create") create:Boolean?
    ):Call<CollectionResponse>


    @FormUrlEncoded
    @POST("collection")
    fun delCollection(
        @Field("token") token:String?,
        @Field("collection_id") collectionId:Int?,
        @Field("delete") delete:Boolean?
    ):Call<CollectionResponse>


    @FormUrlEncoded
    @POST("search")
    fun searchBook(
        @Field("token") token:String?,
        @Field("name") name:String?,
        @Field("critere") critere:String?
    ):Call<List<Book>>


    @GET("books/v1/volumes?")
    fun googleSearch(
        @Query("q") isbn:String?
    ):Call<GoogleBookResponse>


    @FormUrlEncoded
    @POST("proposition")
    fun proposeBook(
        @Field("token") token:String?,
        @Field("title") title:String?,
        @Field("year") year:String?,
        @Field("isbn") isbn: String?,
        @Field("detail") detail:String?
    ):Call<PropositionResponse>

}