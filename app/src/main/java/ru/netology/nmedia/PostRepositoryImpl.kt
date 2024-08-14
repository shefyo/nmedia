package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.Post
import ru.netology.nmedia.PostRepositoryInterface
import java.util.concurrent.TimeUnit

class PostRepositoryImpl : PostRepositoryInterface {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999"
        private val jsonType = "application/json".toMediaType()
    }

    private var posts = listOf<Post>()

    override fun getAll(): List<Post> {
        val request: Request = Request.Builder()
            .url("$BASE_URL/api/slow/posts")
            .build()

        val response = client.newCall(request).execute()
        val body = response.body?.string() ?: throw RuntimeException("body is null")
        posts = gson.fromJson(body, typeToken.type)
        return posts
    }

    override fun getPost(id: Long): Post {
        val request: Request = Request.Builder()
            .url("$BASE_URL/api/slow/posts/$id")
            .build()

        val response = client.newCall(request).execute()
        val body = response.body?.string() ?: throw RuntimeException("body is null")
        return gson.fromJson(body, Post::class.java)
    }

    override fun repostById(id: Long) {
        val request: Request = Request.Builder()
            .post("".toRequestBody(jsonType))
            .url("$BASE_URL/api/slow/posts/$id/repost")
            .build()

        client.newCall(request).execute().close()
    }

    override fun likeById(id: Long): Post {
        val request: Request = Request.Builder()
            .post("".toRequestBody(jsonType))
            .url("$BASE_URL/api/slow/posts/$id/likes")
            .build()

        client.newCall(request)
            .execute()
            .close()

        val post = posts.find { it.id == id }!!
        val updatedPost = post.copy(likedByMe =!post.likedByMe, likes = if (post.likedByMe) post.likes - 1 else post.likes + 1)
        posts = posts.map {
            if (it.id == id) {
                updatedPost
            } else it
        }
        return updatedPost
    }

    override fun save(post: Post) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("$BASE_URL/api/slow/posts")
            .build()

        client.newCall(request).execute().close()
    }

    override fun removeById(id: Long) {
        val request: Request = Request.Builder()
            .delete()
            .url("$BASE_URL/api/slow/posts/$id")
            .build()

        client.newCall(request).execute().close()
    }
}