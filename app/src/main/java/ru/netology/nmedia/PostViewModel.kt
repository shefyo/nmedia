package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import ru.netology.nmedia.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.PostRepositoryInterface
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.IOException
import kotlin.concurrent.thread

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
    private val _post = MutableLiveData<Post?>()
    val post: MutableLiveData<Post?>
        get() = _post

    init {
        loadPosts()
    }

    fun loadPosts() {
        _data.value = FeedModel(loading = true)
        thread {
            try {
                val posts = repository.getAll()
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            } catch (e: IOException) {
                _data.postValue(FeedModel(error = true))
            }
        }
    }

    fun save() {
        edited.value?.let {
            thread {
                repository.save(it)
                _postCreated.postValue(Unit)
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
        thread {
            repository.likeById(id)
            _data.postValue(FeedModel(posts = repository.getAll(), empty = repository.getAll().isEmpty()))
        }
    }

    fun repostById(id: Long) {
        thread {
            repository.repostById(id)
            _post.postValue(repository.getPost(id))
        }
    }

    fun removeById(id: Long) {
        thread {
            val oldPosts = _data.value?.posts.orEmpty()
            val updatedPosts = oldPosts.filter { it.id != id }

            _data.postValue(_data.value?.copy(posts = updatedPosts))

            try {
                repository.removeById(id)
                _postDeleted.postValue(id)
            } catch (e: IOException) {
                _data.postValue(_data.value?.copy(posts = oldPosts))
            }
        }
    }

    fun getPostById(postId: Long) {
        thread {
            _post.postValue(repository.getPost(postId))
        }
    }

    fun loadPost(id: Long) {
        thread {
            _post.postValue(repository.getPost(id))
        }
    }

    fun changeContentAndSave(text: String) {
        thread {
            edited.value?.let {
                if (it.content != text.trim()) {
                    val updatedPost = it.copy(content = text)
                    repository.save(updatedPost)
                    _post.postValue(repository.getPost(it.id))
                }
            }
        }
        edited.value = empty
    }

    fun cancelEditing() {
        edited.value = empty
    }
}
