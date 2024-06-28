package ru.netology.nmedia

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.netology.nmedia.databinding.PostcardBinding


interface OnInteractionListener {
    fun onLike(post: Post)
    fun onRemove(post: Post)
    fun onEdit(post: Post)
    fun onRepost(post: Post)
}

class PostsAdapter(
    private val onInteractionListener: OnInteractionListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostcardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)

            if (post.video != null) {
                holder.binding.videoView.visibility = View.VISIBLE
                holder.binding.playButton.visibility = View.VISIBLE
                holder.binding.videoView.loadUrl(post.video)
                listOf(holder.binding.videoView, holder.binding.playButton).forEach { view ->
                    view.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(post.video))
                    startActivity(view.context, intent, null)
                }
            }
            } else if (post.video == null) {
                holder.binding.videoView.visibility = View.GONE
                holder.binding.playButton.visibility = View.GONE
            }
        }
    }


object PostDiffCallback: DiffUtil.ItemCallback<Post>(){
    override fun areItemsTheSame(oldItem:Post,newItem:Post):Boolean{
        return oldItem.id == newItem.id
    }


    override fun areContentsTheSame(oldItem:Post,newItem:Post):Boolean{
        return oldItem == newItem
    }
}