package ru.netology.nmedia

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: PostViewModel by viewModels {
        PostViewModelFactory(PostRepository())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.loadPost(1)

        viewModel.post.observe(this) { post ->
            binding.apply {
                countreposts.text = formatCount(post.countreposts)
                countviews.text = formatCount(post.countviews)
                author.text = post.author
                published.text = post.published
                content.text = post.content
                countlikes.text = formatCount(post.countlikes)

                likes.setOnClickListener {
                    viewModel.likePost()
                    likes.setImageResource(if (post.likedByMe) R.drawable.like else R.drawable.liked)
                }

                reposts?.setOnClickListener {
                    viewModel.repostPost()
                }
            }
        }
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