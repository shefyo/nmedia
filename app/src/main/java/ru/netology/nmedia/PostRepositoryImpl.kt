package ru.netology.nmedia

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.Post
import ru.netology.nmedia.PostRepositoryInterface
import ru.netology.nmedia.api.PostsApi
import java.lang.RuntimeException

class PostRepositoryImpl : PostRepositoryInterface {

    override fun getAllAsync(callback: PostRepositoryInterface.NMediaCallback<List<Post>>) {
        PostsApi.retrofitService.getAll().enqueue(object : Callback<List<Post>> {
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }
                callback.onSuccess(response.body() ?: throw RuntimeException("Body is null"))
            }

            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    override fun getPostAsync(id: Long, callback: PostRepositoryInterface.NMediaCallback<Post>) {
        PostsApi.retrofitService.getById(id).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }
                callback.onSuccess(response.body() ?: throw RuntimeException("Body is null"))
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    override fun likeByIdAsync(id: Long, likedByMe: Boolean, callback: PostRepositoryInterface.NMediaCallback<Post>) {
        val call = if (likedByMe) {
            PostsApi.retrofitService.dislikeById(id)
        } else {
            PostsApi.retrofitService.likeById(id)
        }

        call.enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }
                callback.onSuccess(response.body() ?: throw RuntimeException("Body is null"))
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    override fun repostByIdAsync(id: Long, repostedByMe: Boolean, callback: PostRepositoryInterface.NMediaCallback<Post>) {
        val call = if (repostedByMe) {
            PostsApi.retrofitService.unrepostById(id)
        } else {
            PostsApi.retrofitService.repostById(id)
        }

        call.enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }
                callback.onSuccess(response.body() ?: throw RuntimeException("Body is null"))
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    override fun removeByIdAsync(id: Long, callback: PostRepositoryInterface.NMediaCallback<Unit>) {
        PostsApi.retrofitService.removeById(id).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }
                callback.onSuccess(Unit)
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                callback.onError(t)
            }
        })
    }

    override fun saveAsync(post: Post, callback: PostRepositoryInterface.NMediaCallback<Post>) {
        PostsApi.retrofitService.save(post).enqueue(object : Callback<Post> {
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    callback.onError(RuntimeException(response.message()))
                    return
                }
                callback.onSuccess(response.body() ?: throw RuntimeException("Body is null"))
            }

            override fun onFailure(call: Call<Post>, t: Throwable) {
                callback.onError(t)
            }
        })
    }
}
