package ru.netology.nmedia

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.PostcardBinding

class PostFragment : Fragment() {

    private val viewModel: PostViewModel by viewModels()

    private var onInteractionListener: OnInteractionListener? = object : OnInteractionListener {
        override fun onEdit(post: Post) {
            viewModel.edit(post)
            findNavController().navigate(R.id.action_postFragment_to_editPostFragment)
        }

        override fun onRemove(post: Post) {
            viewModel.removeById(post.id)
            findNavController().navigateUp()
        }

        override fun onLike(post: Post, view: View) {
            viewModel.likeById(post.id)
        }

        override fun onRepost(post: Post, view: View) {
            viewModel.repostById(post.id)
            val intent = Intent().apply {
                action = Intent.ACTION_SEND
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, post.content)
            }
            val shareIntent = Intent.createChooser(intent, "Share post")
            startActivity(shareIntent)
        }

        override fun onMenuClicked(post: Post, view: View) {
            TODO("Not yet implemented")
        }

        override fun onVideoClicked(post: Post, view: View) {
            TODO("Not yet implemented")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = PostcardBinding.inflate(inflater, container, false)

        // Получение аргумента postId
        val postId =
            arguments?.getLong("postId") ?: throw IllegalArgumentException("No post ID provided")

        viewModel.loadPost(postId)
        viewModel.post.observe(viewLifecycleOwner) { post ->
            with(binding) {
                author.text = post.author
                published.text = post.published
                content.text = post.content
                countviews.text = post.countviews.toString()
                likes.text = post.countlikes.toString()
                reposts.text = post.countreposts.toString()
                likes.isChecked = post.likedByMe

                likes.setOnClickListener {
                    viewModel.likeById(post.id)
                }

                reposts.setOnClickListener {
                    viewModel.repostById(post.id)
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, post.content)
                    }
                    val shareIntent = Intent.createChooser(intent, "Share post")
                    startActivity(shareIntent)
                }

                binding.menu.setOnClickListener { view ->
                    PopupMenu(view.context, view).apply {
                        inflate(R.menu.options_post)
                        setOnMenuItemClickListener { item ->
                            when (item.itemId) {
                                R.id.edit -> {
                                    onInteractionListener?.onEdit(post)
                                    true
                                }

                                R.id.remove -> {
                                    onInteractionListener?.onRemove(post)
                                    true
                                }

                                else -> false
                            }
                        }
                    }.show()
                }
            }
        }

        return binding.root
    }
}