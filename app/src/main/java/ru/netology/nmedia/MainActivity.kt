package ru.netology.nmedia

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.databinding.PostcardBinding
import ru.netology.nmedia.PostsAdapter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels {
            PostViewModelFactory(PostRepository())
        }
        val adapter = PostsAdapter(
            onLikeListener = { viewModel.likeById(it.id) },
            onRepostListener = { viewModel.repostById(it.id) }
        )
        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            adapter.submitList(posts)
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