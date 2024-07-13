package ru.netology.nmedia

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.launch
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.databinding.FragmentFeedBinding

class FeedFragment : Fragment() {

    private lateinit var adapter: PostsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)

        val viewModel: PostViewModel by viewModels(ownerProducer = ::requireParentFragment) {
            PostViewModelFactory(requireActivity().application)
        }

        adapter = PostsAdapter(object : OnInteractionListener {
            override fun onLike(post: Post) {
                viewModel.likeById(post.id)
            }

            override fun onRemove(post: Post) {
                viewModel.removeById(post.id)
            }

            override fun onEdit(post: Post) {
                val action = R.id.action_feedFragment_to_editPostFragment
                findNavController().navigate(action)
                viewModel.edit(post)
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
                val bundle = Bundle().apply {
                    putLong("postId", post.id)
                }
                findNavController().navigate(R.id.action_feedFragment_to_postFragment, bundle)
            }
        })

        binding.list.adapter = adapter
        viewModel.data.observe(viewLifecycleOwner) { posts ->
            val newPost = posts.size > adapter.currentList.size
            adapter.submitList(posts) {
                if (newPost) {
                    binding.list.smoothScrollToPosition(0)
                }
            }
        }

        viewModel.postDeleted.observe(viewLifecycleOwner) { deletedPostId ->
            adapter.notifyItemRemoved(adapter.currentList.indexOfFirst { it.id == deletedPostId })
        }

        binding.save.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }

        return binding.root
    }
}
