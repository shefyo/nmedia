package ru.netology.nmedia

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.PopupMenu
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.databinding.PostcardBinding

class PostViewHolder(
    val binding: PostcardBinding,
    private val onInteractionListener: OnInteractionListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(post: Post) {
        binding.apply {
            author.text = post.author
            published.text = post.published
            content.text = post.content
            countviews.text = post.countviews.toString()
            likes.text = formatCount(post.likes)
            reposts.text = formatCount(post.countreposts)
            Glide.with(avatar.context)
                .load("http://10.0.2.2:9999/avatars/${post.authorAvatar}")
                .circleCrop()
                .into(avatar)
            likes.isChecked = post.likedByMe

            root.setOnClickListener {
                onInteractionListener.onPostClicked(post)
            }

            likes.setOnClickListener {
                onInteractionListener.onLike(post)
            }
            reposts.setOnClickListener {
                onInteractionListener.onRepost(post)
            }

            binding.videoView.visibility = if (post.video != null) View.VISIBLE else View.GONE
            binding.playButton.setOnClickListener {
                post.video?.let { url ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    itemView.context.startActivity(intent)
                }
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