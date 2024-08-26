package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import ru.netology.nmedia.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.PostRepositoryImpl
import ru.netology.nmedia.PostRepositoryInterface
import ru.netology.nmedia.util.SingleLiveEvent
import java.lang.Exception

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    published = "",
    likes = 0,
    countreposts = 0,
    countviews = 1,
    video = null,
    repostedByMe = false,
    authorAvatar = ""
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
        _data.postValue(FeedModel(loading = true))

        repository.getAllAsync(object: PostRepositoryInterface.NMediaCallback<List<Post>> {
            override fun onSuccess(posts: List<Post>) {
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun save() {
        edited.value?.let { post ->
            repository.saveAsync(post, object : PostRepositoryInterface.NMediaCallback<Post> {
                override fun onSuccess(result: Post) {
                    _postCreated.postValue(Unit)
                    loadPosts()
                }

                override fun onError(e: Exception) {
                    _data.postValue(FeedModel(error = true))
                }
            })
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

    fun likeById(id: Long, likedByMe: Boolean) {
        repository.likeByIdAsync(id, likedByMe, object : PostRepositoryInterface.NMediaCallback<Post> {
            override fun onSuccess(result: Post) {
                _data.postValue(
                    FeedModel(
                        posts = _data.value?.posts?.map {
                            if (it.id == id) result else it
                        }.orEmpty(),
                        empty = (_data.value?.posts ?: listOf()).isEmpty()
                    )
                )
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun repostById(id: Long) {
        repository.repostByIdAsync(id, object : PostRepositoryInterface.NMediaCallback<Post> {
            override fun onSuccess(result: Post) {
                _post.postValue(result)
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun removeById(id: Long) {
        val oldPosts = _data.value?.posts.orEmpty()
        val updatedPosts = oldPosts.filter { it.id != id }

        _data.postValue(_data.value?.copy(posts = updatedPosts))

        repository.removeByIdAsync(id, object : PostRepositoryInterface.NMediaCallback<Unit> {
            override fun onSuccess(result: Unit) {

            }

            override fun onError(e: Exception) {
                _data.postValue(_data.value?.copy(posts = oldPosts))
            }
        })
    }

    fun getPostById(postId: Long) {
        repository.getPostAsync(postId, object : PostRepositoryInterface.NMediaCallback<Post> {
            override fun onSuccess(result: Post) {
                _post.postValue(result)
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun loadPost(id: Long) {
        repository.getPostAsync(id, object : PostRepositoryInterface.NMediaCallback<Post> {
            override fun onSuccess(result: Post) {
                _post.postValue(result)
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })
    }

    fun changeContentAndSave(text: String) {
        edited.value?.let { post ->
            if (post.content != text.trim()) {
                val updatedPost = post.copy(content = text)
                repository.saveAsync(updatedPost, object : PostRepositoryInterface.NMediaCallback<Post> {
                    override fun onSuccess(result: Post) {
                        _post.postValue(result)
                        loadPosts()
                    }

                    override fun onError(e: Exception) {
                        _data.postValue(FeedModel(error = true))
                    }
                })
            }
        }
        edited.value = empty
    }

    fun cancelEditing() {
        edited.value = empty
    }
}
