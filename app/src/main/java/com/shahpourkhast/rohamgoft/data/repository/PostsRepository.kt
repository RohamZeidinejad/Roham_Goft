package com.shahpourkhast.rohamgoft.data.repository

import com.shahpourkhast.rohamgoft.data.api.RetrofitInstance
import com.shahpourkhast.rohamgoft.data.model.PostsData

class PostsRepository {

    suspend fun getAllPosts() = RetrofitInstance.api.getAllPosts()

    //--------------------------------------------------------------------------------------

    suspend fun createPost(post: PostsData) = RetrofitInstance.api.createPost(post)

    //--------------------------------------------------------------------------------------

    suspend fun deletePost(postId: String) = RetrofitInstance.api.deletePost(postId)

    //--------------------------------------------------------------------------------------

    suspend fun updatePost(postId: String, post: PostsData) = RetrofitInstance.api.updatePost(postId, post)


}