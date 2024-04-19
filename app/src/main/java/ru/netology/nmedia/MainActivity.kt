package ru.netology.nmedia;

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.Post
import ru.netology.nmedia.PostViewModel
import ru.netology.nmedia.R
import ru.netology.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: PostViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.post.observe(this) { post ->
            binding.apply {
                countreposts.text = formatCount(post.countreposts)
                countviews.text = formatCount(post.countviews)
                author.text = post.author
                published.text = post.published
                content.text = post.content
                countlikes.text = formatCount(post.countlikes)

                likes.setOnClickListener {
                    println("На лайк нажали")
                    post.likedByMe = !post.likedByMe
                    if (post.likedByMe) {
                        post.countlikes++
                    } else {
                        post.countlikes--
                    }
                    countlikes.text = formatCount(post.countlikes)
                    likes.setImageResource(if (post.likedByMe) R.drawable.liked else R.drawable.like)
                }

                reposts?.setOnClickListener {
                    post.repostedByMe = !post.repostedByMe
                    if (post.repostedByMe) {
                        post.countreposts++
                    }
                    countreposts.text = formatCount(post.countreposts)
                }
            }
        }

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

        viewModel.setPost(post)
    }

    private fun formatCount(count: Int): String {
        return when {
            count > 1000000 -> "${count / 1000000}.${(count % 1000000) / 100000}M"
            count == 1000000 -> "${count / 1000000}M"
            count >= 10000 -> "${count / 1000}K"
            count >= 1000 -> "${count / 1000}K"
            else -> count.toString()
        }
    }
}