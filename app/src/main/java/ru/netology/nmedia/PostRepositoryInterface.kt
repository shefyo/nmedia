package ru.netology.nmedia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface PostRepositoryInterface {

    fun getPost(id: Long): Post
    fun likeById(id: Long)
    fun repostById(id: Long)
    fun getAll(): MutableLiveData<List<Post>>
}