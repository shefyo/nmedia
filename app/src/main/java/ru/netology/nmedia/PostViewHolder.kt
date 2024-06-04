package ru.netology.nmedia

import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import ru.netology.nmedia.databinding.PostcardBinding

class PostViewHolder(
    private val binding: PostcardBinding,
    private val onInteractionListener: OnInteractionListener
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
                onInteractionListener.onLike(post)
            }
            reposts.setOnClickListener {
                onInteractionListener.onRepost(post)
            }

            menu.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post)
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.edit -> {
                                onInteractionListener.onEdit(post)
                                true
                            }
                            R.id.remove -> {
                                onInteractionListener.onRemove(post)
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

private fun formatCount(count: Int): String {
    return when {
        count > 1000000 -> "${count / 1000000}.${(count % 1000000) / 100000}M"
        count == 1000000 -> "${count / 1000000}M"
        count >= 10000 -> "${count / 1000}K"
        count >= 1000 -> "${count / 1000}K"
        else -> count.toString()
    }
}
