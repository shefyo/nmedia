package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import ru.netology.nmedia.Post
import ru.netology.nmedia.PostRepositoryInterface
import java.io.IOException
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

    override fun getPostAsync(id: Long, callback: PostRepositoryInterface.NMediaCallback<Post>) {
        val request: Request = Request.Builder()
            .url("$BASE_URL/api/slow/posts/$id")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException("Unexpected response code: ${response.code}"))
                        return
                    }
                    val body = response.body?.string() ?: throw RuntimeException("Body is null")
                    try {
                        val post = gson.fromJson(body, Post::class.java)
                        callback.onSuccess(post)
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e)
            }
        })
    }

    override fun getAllAsync(callback: PostRepositoryInterface.NMediaCallback<List<Post>>) {
        val request: Request = Request.Builder()
            .url("$BASE_URL/api/slow/posts")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException("Unexpected response code: ${response.code}"))
                        return
                    }
                    val body = response.body?.string() ?: throw RuntimeException("Body is null")
                    try {
                        val posts: List<Post> = gson.fromJson(body, typeToken.type)
                        callback.onSuccess(posts)
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e)
            }
        })
    }


    override fun likeByIdAsync(id: Long, likedByMe: Boolean, callback: PostRepositoryInterface.NMediaCallback<Post>) {
        val request: Request = if (likedByMe) {
            Request.Builder()
                .delete()
                .url("$BASE_URL/api/slow/posts/$id/likes")
                .build()
        } else {
            Request.Builder()
                .post("".toRequestBody(jsonType))
                .url("$BASE_URL/api/slow/posts/$id/likes")
                .build()
        }

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException("Unexpected response code: ${response.code}"))
                        return
                    }
                    val body = response.body?.string() ?: throw RuntimeException("Body is null")
                    try {
                        val post = gson.fromJson(body, Post::class.java)
                        callback.onSuccess(post)
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e)
            }
        })
    }

    override fun repostByIdAsync(id: Long, callback: PostRepositoryInterface.NMediaCallback<Post>) {
        val request: Request = Request.Builder()
            .post("".toRequestBody(jsonType))
            .url("$BASE_URL/api/slow/posts/$id/repost")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException("Unexpected response code: ${response.code}"))
                        return
                    }
                    val body = response.body?.string() ?: throw RuntimeException("Body is null")
                    try {
                        val post = gson.fromJson(body, Post::class.java)
                        callback.onSuccess(post)
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e)
            }
        })
    }

    override fun removeByIdAsync(id: Long, callback: PostRepositoryInterface.NMediaCallback<Unit>) {
        val request: Request = Request.Builder()
            .delete()
            .url("$BASE_URL/api/slow/posts/$id")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException("Unexpected response code: ${response.code}"))
                        return
                    }
                    callback.onSuccess(Unit)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e)
            }
        })
    }

    override fun saveAsync(post: Post, callback: PostRepositoryInterface.NMediaCallback<Post>) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("$BASE_URL/api/slow/posts")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        callback.onError(RuntimeException("Unexpected response code: ${response.code}"))
                        return
                    }
                    val body = response.body?.string() ?: throw RuntimeException("Body is null")
                    try {
                        val savedPost = gson.fromJson(body, Post::class.java)
                        callback.onSuccess(savedPost)
                    } catch (e: Exception) {
                        callback.onError(e)
                    }
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                callback.onError(e)
            }
        })
    }
}
