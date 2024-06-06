package com.example.libraryapp

import retrofit2.http.POST
import retrofit2.http.Body
import retrofit2.Call

interface ApiInterface {
    @POST("login")
    fun login(@Body body: Map<String, String>): Call<AuthResponse>

    @POST("register")
    fun register(@Body body: Map<String, String>): Call<AuthResponse>

    @POST("getBooks")
    fun getBooks(@Body body: String): Call<BooksResponse>

    @POST("addBook")
    fun addBook(@Body body: Map<String, String>): Call<BooksResponse>
}