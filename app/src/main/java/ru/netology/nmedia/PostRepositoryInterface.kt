package ru.netology.nmedia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface PostRepositoryInterface {

    suspend fun getPost(id: Long): Post
    suspend fun likeById(id: Long)
    suspend fun repostById(id: Long)
    suspend fun removeById(id: Long)
    suspend fun save(post: Post)
    fun getAll(): LiveData<List<Post>>
}