package ru.netology.nmedia

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String,
    val likedByMe: Boolean,
    val likes: Int,
    val countreposts: Int,
    val repostedByMe: Boolean,
    val countviews: Int,
    val video: String?
)