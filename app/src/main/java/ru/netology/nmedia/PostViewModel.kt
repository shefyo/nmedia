package ru.netology.nmedia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PostViewModel(private val repository: PostRepositoryInterface) : ViewModel() {
    private val _post = MutableLiveData<Post>()
    val post: LiveData<Post>
        get() = _post

    fun loadPost(id: Long) {
        val post = repository.getPost(id)
        _post.value = post
    }

    fun likePost() {
        val post = _post.value ?: return
        val updatedPost = repository.likePost(post)
        _post.value = updatedPost
    }

    fun repostPost() {
        val post = _post.value ?: return
        val updatedPost = repository.repostPost(post)
        _post.value = updatedPost
    }
}