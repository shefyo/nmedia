package ru.netology.nmedia

data class Post(
    val id: Long,
    val author: String,
    val content: String,
    val published: String, var countlikes: Int,
    var countreposts: Int,
    var countviews: Int,
    var likedByMe: Boolean = false,
    var repostedByMe: Boolean = false
)