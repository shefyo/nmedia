package ru.netology.nmedia

import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.PostcardBinding

class PostViewHolder(
    private val binding: PostcardBinding,
    private val onLikeListener: OnLikeListener,
    private val onRepostListener: OnRepostListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            countviews.text = post.countviews.toString()
            countlikes.text = formatCount(post.countlikes)
            countreposts.text = formatCount(post.countreposts)
            likes.setImageResource(
                if (post.likedByMe) R.drawable.liked else R.drawable.like
            )
            likes.setOnClickListener {
                onLikeListener(post)
            }
            reposts.setOnClickListener {
                onRepostListener(post)
            }
        }
    }

    private fun formatCount(count: Int): String { return when {
        count > 1000000 -> "${count / 1000000}.${(count % 1000000) / 100000}M"
        count == 1000000 -> "${count / 1000000}M"
        count >= 10000 -> "${count / 1000}K"
        count >= 1000 -> "${count / 1000}K"
        else -> count.toString()
    }
    }
}