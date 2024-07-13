package ru.netology.nmedia;


import java.util.List;

interface PostDao {
    fun getAll(): MutableList<Post>
    fun savePost(post: Post): Post
    fun likeById(id: Long)
    fun removeById(id: Long)
    fun repostById(id: Long)
}
