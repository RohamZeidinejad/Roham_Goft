package com.shahpourkhast.rohamgoft.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shahpourkhast.rohamgoft.data.model.PostsData
import com.shahpourkhast.rohamgoft.databinding.PostItemBinding

class PostsAdapter : ListAdapter<PostsData, PostsAdapter.MovieViewHolder>(PostsDiffCallback()) {

    var onPostClick: ((PostsData) -> Unit)? = null

    var onPostLongClick: ((PostsData) -> Unit)? = null

    inner class MovieViewHolder(val binding: PostItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBindCodes(postsData: PostsData) {

            binding.author.text = postsData.author
            binding.content.text = postsData.content

            //-----------------------------------------------------------

            itemView.setOnClickListener {

                onPostClick?.invoke(getItem(adapterPosition))

            }

            //-----------------------------------------------------------

            itemView.setOnLongClickListener {

                onPostLongClick?.invoke(getItem(adapterPosition))

                true

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {

        val binding = PostItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MovieViewHolder(binding)

    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {

        holder.onBindCodes(getItem(position))

    }

}


class PostsDiffCallback : DiffUtil.ItemCallback<PostsData>() {

    override fun areItemsTheSame(oldItem: PostsData, newItem: PostsData): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PostsData, newItem: PostsData): Boolean {
        return oldItem == newItem
    }

}
