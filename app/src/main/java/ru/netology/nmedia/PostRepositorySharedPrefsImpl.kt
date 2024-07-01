package ru.netology.nmedia

import android.content.Context
import android.provider.Settings.Global.putString
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Collections.list

class PostRepositorySharedPrefsImpl(context: Context) : PostRepositoryInterface {

    companion object {
        private const val KEY = "posts"
    }
    private val gson = Gson()
    private val prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE)
    private val typeToken = TypeToken.getParameterized(List::class.java, Post::class.java).type
    private var nextId: Long = 0
    private var posts = emptyList<Post>()
    private var deafultPosts = listOf(
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия— помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            countlikes = 999999,
            countreposts = 0,
            countviews = 1,
            likedByMe = false,
            video = "https://youtu.be/ix280JoeyBs?si=K_aiKhiI49y7H6mH",
            repostedByMe = false
        ), Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Знаний хватит на всех: на следующей неделе будет что-то",
            published = "18 сентября в 10:12",
            countlikes = 999999,
            countreposts = 0,
            countviews = 1,
            likedByMe = false,
            video = "https://youtu.be/ix280JoeyBs?si=K_aiKhiI49y7H6mH",
            repostedByMe = false
        )
    ).reversed()

    private val data = MutableLiveData(posts)

    init {
        val savedPosts = prefs.getString(KEY, null)?.let {
            gson.fromJson(it, typeToken) as List<Post>
        } ?: emptyList()

        nextId = (savedPosts.maxByOrNull { it.id }?.id ?: 0) + 1

        posts = (deafultPosts + savedPosts).distinctBy { it.id }.sortedByDescending { it.id }

        data.value = posts
        sync()
    }



    private fun sync() {
        with(prefs.edit()) {
            putString(KEY, gson.toJson(posts))
            apply()
        }
    }

    override fun getPost(id: Long): Post {
        return posts.find { it.id == id } ?: throw IllegalArgumentException("Пост не найден")
        sync()
    }

    fun checkVideo(post: Post) {
        if(post.video.toString().contains("https://youtu.be/")) {
            post.video != null
        }
    }


    override fun likeById(id: Long) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(
                    likedByMe = !post.likedByMe,
                    countlikes = if (post.likedByMe) post.countlikes - 1 else post.countlikes + 1
                )
            } else {
                post
            }
        }
        data.value = posts
        sync()
    }

    override fun repostById(id: Long) {
        posts = posts.map { post ->
            if (post.id == id) {
                post.copy(
                    repostedByMe = !post.repostedByMe,
                    countreposts = if (post.repostedByMe) post.countreposts + 1 else post.countreposts + 0
                )
            } else {
                post
            }
        }
        data.value = posts
        sync()
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
        sync()
    }

    override fun save(post: Post) {
        posts = if (post.id == 0L) {
            listOf(
                post.copy(
                    id = nextId++,
                    author = "Me",
                    likedByMe = false,
                    published = "Now"
                )
            ) + posts
        } else {
            posts.map {if (it.id == post.id) it.copy (content = post.content) else it}
        }
        data.value = posts
        sync()
    }

    override fun getAll(): MutableLiveData<List<Post>> = data
}