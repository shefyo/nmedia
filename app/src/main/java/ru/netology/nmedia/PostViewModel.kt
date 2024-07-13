package ru.netology.nmedia

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.PostRepositoryImpl

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    published = "",
    countlikes = 0,
    countreposts = 0,
    countviews = 1,
    video = null,
    repostedByMe = false
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: PostRepositoryInterface = PostRepositoryImpl(
        AppDb.getInstance(context = application).postDao()
    )
    val data = this.repository.getAll()
    val edited = MutableLiveData(empty)
    private val _postDeleted = MutableLiveData<Long>()
    val postDeleted: LiveData<Long> = _postDeleted
    fun likeById(id: Long) {
        viewModelScope.launch {
            repository.likeById(id)
            _post.value = repository.getPost(id)
        }
    }

    fun repostById(id: Long) {
        viewModelScope.launch {
            repository.repostById(id)
            _post.value = repository.getPost(id)
        }
    }

    fun updatePost(post: Post) {
        viewModelScope.launch {
            repository.save(post)
            _post.value = repository.getPost(post.id)
        }
    }

    fun removeById(id: Long) {
        viewModelScope.launch {
            repository.removeById(id)
            _postDeleted.value = id
        }
    }

    private val _post = MutableLiveData<Post>()
    val post: LiveData<Post>
        get() = _post

    fun edit(post: Post) {
        edited.value = post
        val _isEditing = MutableLiveData<Boolean>(false)
        _isEditing.value = true
    }

    fun getPostById(postId: Long) {
        viewModelScope.launch {
            repository.getPost(postId)
        }
    }

    fun loadPost(id: Long) {
        viewModelScope.launch {
            val post = repository.getPost(id)
            _post.value = post
        }
    }


    fun changeContentAndSave(text: String) {
        viewModelScope.launch {
            edited.value?.let {
                if (it.content != text.trim()) {
                    repository.save(it.copy(content = text))
                    _post.value = repository.getPost(it.id)
                }
            }
        }


        val _isEditing = MutableLiveData<Boolean>(false)
        val isEditing: LiveData<Boolean> = _isEditing

        fun cancelEditing() {
            _isEditing.value = false
            edited.value = empty
        }


    }
}