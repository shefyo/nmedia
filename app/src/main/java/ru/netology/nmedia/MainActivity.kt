package ru.netology.nmedia

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import ru.netology.nmedia.databinding.ActivityMainBinding
import ru.netology.nmedia.databinding.PostcardBinding
import ru.netology.nmedia.PostsAdapter
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.util.AndroidUtils.focusAndShowKeyboard

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewModel: PostViewModel by viewModels {
            PostViewModelFactory(PostRepository())
        }
        val adapter = PostsAdapter(object: OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onEdit(post: Post) {
                viewModel.edit(post)
            }

            override fun onRepost(post: Post) {
                viewModel.repostById(post.id)
            }

        })
        binding.list.adapter = adapter
        viewModel.data.observe(this) { posts ->
            val newPost = posts.size > adapter.currentList.size
            adapter.submitList(posts) {
                if (newPost) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }

        viewModel.edited.observe(this) { post ->
            with(binding) {
                if (post.id == 0L) {
                    group.visibility = View.GONE
                } else {
                    group.visibility = View.VISIBLE
                    content.setText(post.content)
                    content.focusAndShowKeyboard()
                }
            }
        }

        binding.cancel.setOnClickListener {
            viewModel.cancelEditing()
            binding.content.setText("")
            AndroidUtils.hideKeyboard(binding.content)
            binding.content.clearFocus()
        }

        binding.save.setOnClickListener {
            val content = binding.content.text.toString()
            if (content.isBlank()) {
                Toast.makeText(this, "Error empty content", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            viewModel.changeContentAndSave(content)
            binding.content.setText("")
            binding.group.visibility = View.GONE
            AndroidUtils.hideKeyboard(binding.content)
            binding.content.clearFocus()
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