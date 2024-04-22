package ru.netology.nmedia

interface PostRepositoryInterface {
    fun getPost(id: Long): Post
    fun likePost(post: Post): Post
    fun repostPost(post: Post): Post
}
