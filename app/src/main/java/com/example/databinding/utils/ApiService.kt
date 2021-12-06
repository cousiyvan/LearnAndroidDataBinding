package com.example.databinding.utils

import com.example.databinding.models.Post
import com.example.databinding.models.User
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET("posts")
    suspend fun getPosts(): Response<MutableList<Post>>

    @GET("posts/{num}}")
    suspend fun getPostById(@Path("num") num: Int): Response<Post>

    @POST("users")
    suspend fun createPost(@Body post: Post): Response<Post>
}