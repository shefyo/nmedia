package ru.netology.nmedia

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
    repostedByMe = false
)

class PostViewModel(repository: PostRepositoryInterface) : ViewModel() {
    private val repository: PostRepositoryInterface = PostRepository()
    val data = this.repository.getAll()
    val edited = MutableLiveData(empty)
    fun likeById(id: Long) = repository.likeById(id)
    fun repostById(id: Long) = repository.repostById(id)
    fun removeById(id: Long) = repository.removeById(id)
    private val _post = MutableLiveData<Post>()
    val post: LiveData<Post>
        get() = _post

    fun edit(post: Post) {
        edited.value = post
        _isEditing.value = true
    }

    fun changeContentAndSave(text: String) {
        edited.value?.let {
            if(it.content != text.trim()) {
                repository.save(it.copy(content = text))
            }
        }
    }

    fun loadPost(id: Long) {
        val post = repository.getPost(id)
        _post.value = post
    }

    private val _isEditing = MutableLiveData<Boolean>(false)
    val isEditing: LiveData<Boolean> = _isEditing

    fun cancelEditing() {
        _isEditing.value = false
        edited.value = empty
    }


}