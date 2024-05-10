package ru.netology.nmedia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PostViewModel(repository: PostRepositoryInterface) : ViewModel() {
    private val repository: PostRepositoryInterface = PostRepository()
    val data = this.repository.getAll()
    fun likeById(id: Long) = repository.likeById(id)
    fun repostById(id: Long) = repository.repostById(id)
    private val _post = MutableLiveData<Post>()
    val post: LiveData<Post>
        get() = _post

    fun loadPost(id: Long) {
        val post = repository.getPost(id)
        _post.value = post
    }


}