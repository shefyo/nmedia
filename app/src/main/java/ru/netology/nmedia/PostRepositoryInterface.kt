package ru.netology.nmedia

interface PostRepositoryInterface {

    fun getAllAsync(callback: NMediaCallback<List<Post>>)
    fun getPostAsync(id: Long, callback: NMediaCallback<Post>)
    fun likeByIdAsync(id: Long, likedByMe: Boolean, callback: NMediaCallback<Post> )
    fun repostByIdAsync(id: Long, repostedByMe: Boolean, callback: NMediaCallback<Post>)
    fun removeByIdAsync(id: Long, callback: NMediaCallback<Unit>)
    fun saveAsync(post: Post, callback: NMediaCallback<Post>)

    interface NMediaCallback<T> {
        fun onSuccess(data: T)
        fun onError(e: Throwable)
    }
}