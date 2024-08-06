package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import ru.netology.nmedia.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.PostRepositoryInterface
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.IOException

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
    private val repository: PostRepositoryInterface = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated
    private val _postDeleted = MutableLiveData<Long>()
    val postDeleted: LiveData<Long> = _postDeleted
    private val _post = MutableLiveData<Post>()
    val post: LiveData<Post>
        get() = _post

    init {
        loadPosts()
    }

    fun loadPosts() {
        _data.value = FeedModel(loading = true)
        viewModelScope.launch {
            try {
                val posts = repository.getAll()
                _data.value = FeedModel(posts = posts.value ?: emptyList(), empty = posts.value.isNullOrEmpty())
            } catch (e: IOException) {
                _data.value = FeedModel(error = true)
            }
        }
    }


    fun save() {
        edited.value?.let {
            viewModelScope.launch {
                repository.save(it)
                _postCreated.value = Unit
                _post.value = repository.getPost(it.id)
            }
        }
        edited.value = empty
    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }

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

    fun removeById(id: Long) {
        viewModelScope.launch {
            val oldPosts = _data.value?.posts.orEmpty()
            val updatedPosts = oldPosts.filter { it.id != id }

            _data.value = _data.value?.copy(posts = updatedPosts)

            try {
                repository.removeById(id)
                _postDeleted.value = id
            } catch (e: IOException) {
                _data.value = _data.value?.copy(posts = oldPosts)
            }
        }
    }


    fun getPostById(postId: Long) {
        viewModelScope.launch {
            _post.value = repository.getPost(postId)
        }
    }

    fun loadPost(id: Long) {
        viewModelScope.launch {
            _post.value = repository.getPost(id)
        }
    }

    fun changeContentAndSave(text: String) {
        viewModelScope.launch {
            edited.value?.let {
                if (it.content != text.trim()) {
                    val updatedPost = it.copy(content = text)
                    repository.save(updatedPost)
                    _post.value = repository.getPost(it.id)
                }
            }
        }
        edited.value = empty
    }

    fun cancelEditing() {
        edited.value = empty
    }
}
