package ru.netology.nmedia

import androidx.lifecycle.MutableLiveData

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val countlikes: Int,
    val countreposts: Int,
    val countviews: Int,
    val likedByMe: Boolean,
    val repostedByMe: Boolean,
)