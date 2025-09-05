package com.shahpourkhast.rohamgoft.data.repository

import com.shahpourkhast.rohamgoft.data.api.ApiService
import com.shahpourkhast.rohamgoft.data.model.PostsData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostsRepository @Inject constructor(private val api : ApiService) {

    suspend fun getAllPosts() = api.getAllPosts()

    //--------------------------------------------------------------------------------------

    suspend fun createPost(post: PostsData) = api.createPost(post)

    //--------------------------------------------------------------------------------------

    suspend fun deletePost(postId: String) = api.deletePost(postId)

    //--------------------------------------------------------------------------------------

    suspend fun updatePost(postId: String, post: PostsData) = api.updatePost(postId, post)


}