package com.shahpourkhast.rohamgoft.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahpourkhast.rohamgoft.data.model.PostsData
import com.shahpourkhast.rohamgoft.data.repository.PostsRepository
import kotlinx.coroutines.launch

class PostsViewModel : ViewModel() {

    private val repository = PostsRepository()

    //----------------------------------------------------------------------

    private val _posts = MutableLiveData<List<PostsData>>()
    val posts: LiveData<List<PostsData>> = _posts

    //----------------------------------------------------------------------

    private val _createPostStatus = MutableLiveData<Boolean>()
    val createPostStatus: LiveData<Boolean> = _createPostStatus

    //----------------------------------------------------------------------

    private val _deletePostStatus = MutableLiveData<Boolean>()
    val deletePostStatus: LiveData<Boolean> = _deletePostStatus

    //----------------------------------------------------------------------

    private val _isLoadingLiveData = MutableLiveData<Boolean>()
    val isLoadingLiveData: LiveData<Boolean> = _isLoadingLiveData

    //----------------------------------------------------------------------

    init {

        getAllPosts()

    }

    //----------------------------------------------------------------------

    fun getAllPosts() = viewModelScope.launch {

        try {

            _isLoadingLiveData.postValue(true)

            val response = repository.getAllPosts()
            if (response.isSuccessful) _posts.postValue(response.body())

            _isLoadingLiveData.postValue(false)

        } catch (e: Exception) {

            Log.v("getAllPosts_error" , e.message.toString())

        }
    }

    //----------------------------------------------------------------------

    fun createPost(author: String, content: String) = viewModelScope.launch {

        val newPost = PostsData(id = "", createdAt = "", author = author, content = content)

        try {

            val response = repository.createPost(newPost)
            if (response.isSuccessful) {

                _createPostStatus.postValue(true)

                getAllPosts()

            } else {

                Log.e("PostViewModel", "Error creating post: ${response.errorBody()?.string()}")
                _createPostStatus.postValue(false)

            }
        } catch (e: Exception) {

            Log.e("PostViewModel", e.message.toString())
            _createPostStatus.postValue(false)

        }

    }

    //----------------------------------------------------------------------

    fun deletePost(postId: String) = viewModelScope.launch {

        try {

            val response = repository.deletePost(postId)
            if (response.isSuccessful) {

                _deletePostStatus.postValue(true)

                getAllPosts()

            } else {

                Log.e("PostsViewModel", "Error deleting post: ${response.errorBody()?.string()}")
                _deletePostStatus.postValue(false)

            }
        } catch (e: Exception) {

            Log.e("PostsViewModel", e.message.toString())
            _deletePostStatus.postValue(false)

        }

    }

    //----------------------------------------------------------------------

    fun updatePost(postId: String, postToUpdate: PostsData) = viewModelScope.launch {

        try {

            val response = repository.updatePost(postId, postToUpdate)
            if (response.isSuccessful) {

                getAllPosts()

            } else {

                Log.e("PostsViewModel", "Error updating post: ${response.errorBody()?.string()}")

            }
        } catch (e: Exception) {

            Log.e("PostsViewModel", e.message.toString())

        }
    }



}