package ru.netology.nmedia

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.PostcardBinding
import ru.netology.nmedia.viewmodel.PostViewModel

class PostFragment : Fragment() {

    private val viewModel: PostViewModel by activityViewModels()

    private var onInteractionListener: OnInteractionListener? = object : OnInteractionListener {
        override fun onEdit(post: Post) {
            viewModel.edit(post)
            findNavController().navigate(R.id.action_postFragment_to_editPostFragment)
        }

        override fun onRemove(post: Post) {
            viewModel.removeById(post.id)
            findNavController().navigate(R.id.action_postFragment_to_feedFragment)
        }

        override fun onLike(post: Post) {
            viewModel.likeById(post.id, post.likedByMe)
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

        override fun onPostClicked(post: Post) {
            TODO("Not implemented")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = PostcardBinding.inflate(inflater, container, false)

        val postId = arguments?.getLong("postId") ?: throw IllegalArgumentException("No post ID provided")

        viewModel.loadPost(postId)
        viewModel.post.observe(viewLifecycleOwner) { post ->
            post?.let {
                with(binding) {
                    author.text = post.author
                    published.text = post.published
                    content.text = post.content
                    countviews.text = post.countviews.toString()
                    likes.text = post.likes.toString()
                    reposts.text = post.countreposts.toString()
                    likes.isChecked = post.likedByMe

                    likes.setOnClickListener {
                        viewModel.likeById(post.id, post.likedByMe)
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
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<String>("postKey")
            ?.observe(viewLifecycleOwner) { result ->
                result?.let {
                    val updatedPost = viewModel.post.value?.copy(content = it)
                }
            }

        return binding.root
    }
}
