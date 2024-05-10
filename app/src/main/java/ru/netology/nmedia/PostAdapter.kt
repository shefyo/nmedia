package ru.netology.nmedia

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import ru.netology.nmedia.databinding.PostcardBinding

typealias OnLikeListener = (post: Post) -> Unit
typealias OnRepostListener = (post: Post) -> Unit

class PostsAdapter(
    private val onLikeListener: OnLikeListener,
    private val onRepostListener: OnRepostListener
) : ListAdapter<Post, PostViewHolder>(PostDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = PostcardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onLikeListener, onRepostListener)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
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