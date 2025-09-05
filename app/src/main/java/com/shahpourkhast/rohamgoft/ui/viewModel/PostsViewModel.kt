package com.shahpourkhast.rohamgoft.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shahpourkhast.rohamgoft.data.model.PostsData
import com.shahpourkhast.rohamgoft.data.repository.PostsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface PostsState {
    data object Idle : PostsState
    data object Loading : PostsState
    data class Success(val data: List<PostsData>) : PostsState
    data class Error(val message: String) : PostsState
}

@HiltViewModel
class PostsViewModel @Inject constructor(private val repository: PostsRepository) : ViewModel() {

    //----------------------------------------------------------------------

    private val _posts = MutableStateFlow<PostsState>(PostsState.Idle)
    val posts: StateFlow<PostsState> = _posts

    //----------------------------------------------------------------------

    private val _createPostStatus = MutableSharedFlow<Boolean>()
    val createPostStatus: SharedFlow<Boolean> = _createPostStatus

    //----------------------------------------------------------------------

    private val _deletePostStatus = MutableSharedFlow<Boolean>()
    val deletePostStatus: SharedFlow<Boolean> = _deletePostStatus

    //----------------------------------------------------------------------

    init {

        getAllPosts()

    }

    //----------------------------------------------------------------------

    fun getAllPosts() = viewModelScope.launch {

        _posts.value = PostsState.Loading

        try {

            val response = repository.getAllPosts()
            if (response.isSuccessful && response.body() != null) _posts.value = PostsState.Success(response.body()!!)

        } catch (e: Exception) {

            _posts.value = PostsState.Error(e.message ?: "Unknown Error!")

        }

    }

    //----------------------------------------------------------------------

    fun createPost(author: String, content: String) = viewModelScope.launch {

        val newPost = PostsData(id = "", createdAt = "", author = author, content = content)

        try {

            val response = repository.createPost(newPost)
            if (response.isSuccessful) {

                _createPostStatus.emit(true)

                getAllPosts()

            }


        } catch (e: Exception) {

            _posts.value = PostsState.Error(e.message ?: "Unknown Error!")

        }

    }

    //----------------------------------------------------------------------

    fun deletePost(postId: String) = viewModelScope.launch {

        try {

            val response = repository.deletePost(postId)
            if (response.isSuccessful) {

                _deletePostStatus.emit(true)

                getAllPosts()

            }
        }

        catch (e: Exception) {

            Log.e("PostsViewModel", e.message.toString())

        }

    }

    //----------------------------------------------------------------------

    fun updatePost(postId: String, post: PostsData) = viewModelScope.launch {

        try {

            val response = repository.updatePost(postId, post)
            if (response.isSuccessful) {

                getAllPosts()

            }

        } catch (e: Exception) {

            _posts.value = PostsState.Error(e.message ?: "Unknown Error!")

        }
    }



}