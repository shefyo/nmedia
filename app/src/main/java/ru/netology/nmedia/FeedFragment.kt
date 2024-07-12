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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentFeedBinding.inflate(inflater, container, false)


            val viewModel: PostViewModel by viewModels (ownerProducer = ::requireParentFragment) {
                PostViewModelFactory(requireActivity().application)
            }


            fun handleIntent(intent: Intent?) {
                if (intent?.action == Intent.ACTION_VIEW) {
                    val videoUrl = intent.dataString
                    if (!videoUrl.isNullOrEmpty()) {

                    }
                }
            }



            handleIntent(requireActivity().intent)


            val adapter = PostsAdapter(object : OnInteractionListener {
                override fun onLike(post: Post, view: View) {
                    viewModel.likeById(post.id)

                    val bundle = Bundle().apply {
                        putLong("postId", post.id)
                        putString("postContent", post.content)
                        putInt("countLikes", post.countlikes)
                        putInt("countReposts", post.countreposts)
                        putInt("countViews", post.countviews)
                    }
                    findNavController().navigate(R.id.action_feedFragment_to_postFragment, bundle)
                }

                override fun onRemove(post: Post) {
                    viewModel.removeById(post.id)
                }


                override fun onEdit(post: Post) {
                    val action = R.id.action_feedFragment_to_editPostFragment
                    findNavController().navigate(action)
                    viewModel.edit(post)
                }

                override fun onMenuClicked(post: Post, view: View) {
                    val bundle = Bundle().apply {
                        putLong("postId", post.id)
                        putString("postContent", post.content)
                        putInt("countLikes", post.countlikes)
                        putInt("countReposts", post.countreposts)
                        putInt("countViews", post.countviews)
                    }
                    findNavController().navigate(R.id.action_feedFragment_to_postFragment, bundle)
                }

                override fun onVideoClicked(post: Post, view: View) {
                    val bundle = Bundle().apply {
                        putLong("postId", post.id)
                        putString("postContent", post.content)
                        putInt("countLikes", post.countlikes)
                        putInt("countReposts", post.countreposts)
                        putInt("countViews", post.countviews)
                    }
                    findNavController().navigate(R.id.action_feedFragment_to_postFragment, bundle)

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

                    val bundle = Bundle().apply {
                        putLong("postId", post.id)
                        putString("postContent", post.content)
                        putInt("countLikes", post.countlikes)
                        putInt("countReposts", post.countreposts)
                        putInt("countViews", post.countviews)
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


        binding.save.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }



        return binding.root
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