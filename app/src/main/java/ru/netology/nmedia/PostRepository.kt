package ru.netology.nmedia

class PostRepository : PostRepositoryInterface {
    private val posts = mutableMapOf<Long, Post>()

    init {
        val post = Post(
            id = 1,
            author = "Нетология. Университет интернет-профессий будущего",
            content = "Привет, это новая Нетология! Когда-то Нетология начиналась с интенсивов по онлайн-маркетингу. Затем появились курсы по дизайну, разработке, аналитике и управлению. Мы растём сами и помогаем расти студентам: от новичков до уверенных профессионалов. Но самое важное остаётся с нами: мы верим, что в каждом уже есть сила, которая заставляет хотеть больше, целиться выше, бежать быстрее. Наша миссия— помочь встать на путь роста и начать цепочку перемен → http://netolo.gy/fyb",
            published = "21 мая в 18:36",
            countlikes = 999999,
            countreposts = 0,
            countviews = 1,
            likedByMe = false,
            repostedByMe = false,
        )
        posts[post.id] = post
    }

    override fun getPost(id: Long): Post {
        return posts[id] ?: throw IllegalArgumentException("Пост не найден")
    }

    override fun likePost(post: Post): Post {
        val updatedPost = post.copy(likedByMe = !post.likedByMe, countlikes = if (post.likedByMe) post.countlikes - 1 else post.countlikes + 1)
        posts[post.id] = updatedPost
        return updatedPost
    }

    override fun repostPost(post: Post): Post {
        val updatedPost = post.copy(repostedByMe = !post.repostedByMe, countreposts = if (post.repostedByMe) post.countreposts +1 else post.countreposts + 0)
        posts[post.id] = updatedPost
        return updatedPost
    }
}