package com.asterisk.movieworld.shared

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.asterisk.movieworld.data.services.tdbmmodel.Result
import com.asterisk.movieworld.databinding.MovieItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class MovieWorldAdapter(
    private val onItemClicked: (Result) -> Unit
) : RecyclerView.Adapter<MovieWorldAdapter.MovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layout = MovieItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MovieViewHolder(layout, onItemClicked = { position ->
            val result = differ.currentList[position]
            if (result != null) {
                onItemClicked(result)
            }
        })
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentItem = differ.currentList[position]
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    inner class MovieViewHolder(
        private val binding: MovieItemBinding,
        private val onItemClicked: (Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(result: Result) {
            binding.apply {
                Glide.with(itemView)
                    .load("https://image.tmdb.org/t/p/w342/${result.poster_path}")
                    .error(ColorDrawable(Color.RED))
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(ivImageTvShow)

                tvOriginalTitle.text = result.originalTitle
                tvReleaseDate.text = result.releaseDate
                tvAdult.text = result.adult.toString()
                tvVoteCount.text = result.voteCount.toString()
            }
        }

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val item = differ.currentList[position]
                        if (item != null) {
                            onItemClicked(position)
                        }
                    }
                }
            }
        }
    }


    private val diffCallback = object : DiffUtil.ItemCallback<Result>() {
        override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean =
            oldItem == newItem
    }

    val differ = AsyncListDiffer(this, diffCallback)
}