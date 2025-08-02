package com.shahpourkhast.rohamgoft.data.api

import com.shahpourkhast.rohamgoft.data.model.PostsData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @GET("posts")
    suspend fun getAllPosts(): Response<List<PostsData>>

    //--------------------------------------------------------------------

    @POST("posts")
    suspend fun createPost(@Body post: PostsData): Response<PostsData>

    //--------------------------------------------------------------------

    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") postId: String): Response<Unit>

    //--------------------------------------------------------------------

    @PUT("posts/{id}")
    suspend fun updatePost(@Path("id") postId: String, @Body post: PostsData): Response<PostsData>

}