package ru.netology.nmedia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PostRepositorySQLiteImpl(
    private val dao: PostDao
) : PostRepositoryInterface {
    private var posts = emptyList<Post>()
    private val data = MutableLiveData(posts)

    init {
        posts = dao.getAll()
        data.value = posts
    }

    override fun getAll(): MutableLiveData<List<Post>> = data
    override fun getPost(id: Long): Post {
        return posts.find { it.id == id } ?: throw IllegalArgumentException("Пост не найден")
    }

    override fun likeById(id: Long) {
        dao.likeById(id)
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(
                    likedByMe = !post.likedByMe,
                    countlikes = if (post.likedByMe) post.countlikes - 1 else post.countlikes + 1
                )
            } else {
                post
            }
        }
        data.value = posts
    }

    override fun repostById(id: Long) {
        dao.repostById(id)
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(
                    repostedByMe = !post.repostedByMe,
                    countreposts = if (post.repostedByMe) post.countreposts + 1 else post.countreposts + 0
                )
            } else {
                post
            }
        }
        data.value = posts
    }

    override fun removeById(id: Long) {
        dao.removeById(id)
        posts = posts.filter { it.id != id }
        data.value = posts
    }

    override fun save(post: Post) {
        val id = post.id
        val saved = dao.savePost(post)
        posts = if (id == 0L) {
            listOf(saved) + posts
        } else {
            posts.map {
                if (it.id != id) it else saved
            }
        }
        data.value = posts
    }
}
