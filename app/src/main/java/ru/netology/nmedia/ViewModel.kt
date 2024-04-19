package ru.netology.nmedia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.netology.nmedia.Post

class PostViewModel : ViewModel() {
    private val _post = MutableLiveData<Post>()
    val post: LiveData<Post>
        get() = _post

    fun setPost(post: Post) {
        _post.value = post
    }
}