package ru.netology.nmedia

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val countlikes: Int,
    val countreposts: Int,
    val countviews: Int,
    var video: String?,
    val likedByMe: Boolean,
    val repostedByMe: Boolean,
)