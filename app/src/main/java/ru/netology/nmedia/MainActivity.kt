package ru.netology.nmedia

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.launch
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

        fun handleIntent(intent: Intent?) {
            if (intent?.action == Intent.ACTION_VIEW){
                val videoUrl = intent.dataString

                if (!videoUrl.isNullOrEmpty()) {

                }
            }
        }

        handleIntent(intent)


        val editPostLauncher = registerForActivityResult(EditPostContract) {
            var result = it ?: return@registerForActivityResult
            viewModel.changeContentAndSave(result)
        }

        val newPostLauncher = registerForActivityResult(NewPostContract) {
            var result = it ?: return@registerForActivityResult
            viewModel.changeContentAndSave(result)
        }
        val adapter = PostsAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onEdit(post: Post) {
                editPostLauncher.launch(post.content)
            }

            override fun onRepost(post: Post) {
                viewModel.repostById(post.id)
                val intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, post.content)
                }

                val shareIntent = Intent.createChooser(intent, "Share post")
                startActivity(shareIntent)
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

        binding.save.setOnClickListener {
           newPostLauncher.launch()
   }

        fun formatCount(count: Int): String {
            return when {
                count > 1000000 -> "${count / 1000000}.${(count % 1000000) / 100000}M"
                count == 1000000 -> "${count / 1000000}M"
                count >= 10000 -> "${count / 1000}K"
                count >= 1000 -> "${count / 1000}K"
                else -> count.toString()
            }
        }
    }
}