package ru.netology.nmedia

import androidx.lifecycle.MutableLiveData

class PostRepository : PostRepositoryInterface {
    private var nextId: Long = 0
    private var posts = listOf(
        Post(
            id = nextId++,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия— помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            countlikes = 999999,
            countreposts = 0,
            countviews = 1,
            likedByMe = false,
            video = null,
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
            video = null,
            repostedByMe = false
        )
    )

    private val data = MutableLiveData(posts)

    override fun getPost(id: Long): Post {
        return posts.find { it.id == id } ?: throw IllegalArgumentException("Пост не найден")
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
    }

    override fun removeById(id: Long) {
        posts = posts.filter { it.id != id }
        data.value = posts
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
    }

    override fun getAll(): MutableLiveData<List<Post>> = data
}