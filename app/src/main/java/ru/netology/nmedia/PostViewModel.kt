package ru.netology.nmedia

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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
    private val repository: PostRepositoryInterface = PostRepositorySQLiteImpl(AppDb.getInstance(application).postDao)
    val data = this.repository.getAll()
    val edited = MutableLiveData(empty)
    private val _postDeleted = MutableLiveData<Long>()
    val postDeleted: LiveData<Long> = _postDeleted
    fun likeById(id: Long)  {
        repository.likeById(id)
        _post.value = repository.getPost(id)
    }
    fun repostById(id: Long) {
        repository.repostById(id)
        _post.value = repository.getPost(id)
    }

    fun updatePost(post: Post) {
        repository.save(post)
        _post.value = repository.getPost(post.id)
    }
    fun removeById(id: Long) {
        repository.removeById(id)
        _postDeleted.value = id
    }
    private val _post = MutableLiveData<Post>()
    val post: LiveData<Post>
        get() = _post

    fun edit(post: Post) {
        edited.value = post
        _isEditing.value = true
    }

    fun getPostById(postId: Long): Post {
        return repository.getPost(postId)
    }

    fun loadPost(id: Long) {
        val post = repository.getPost(id)
        _post.value = post
    }


    fun changeContentAndSave(text: String) {
        edited.value?.let {
            if (it.content != text.trim()) {
                repository.save(it.copy(content = text))
                _post.value = repository.getPost(it.id)
            }
        }
    }




    private val _isEditing = MutableLiveData<Boolean>(false)
    val isEditing: LiveData<Boolean> = _isEditing

    fun cancelEditing() {
        _isEditing.value = false
        edited.value = empty
    }


}