package com.asterisk.movieworld.ui.details

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.asterisk.movieworld.data.services.tdbmmodel.ResultXX
import com.asterisk.movieworld.databinding.SimilarMovieItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class SimilarMovieAdapter() : RecyclerView.Adapter<SimilarMovieAdapter.SimilarMovieViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SimilarMovieViewHolder {
        val layout =
            SimilarMovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SimilarMovieViewHolder(layout)
    }

    override fun onBindViewHolder(holder: SimilarMovieViewHolder, position: Int) {
        val movie = differ.currentList[position]
        if (movie != null) {
            holder.bind(movie)
        }
    }

    override fun getItemCount() = differ.currentList.size


    inner class SimilarMovieViewHolder(private val binding: SimilarMovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(resultXX: ResultXX) {

            binding.apply {
                Glide.with(itemView)
                    .load("https://image.tmdb.org/t/p/w342/${resultXX.posterPath}")
                    .error(ColorDrawable(Color.RED))
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(ivImageTvShow)
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<ResultXX>() {
        override fun areItemsTheSame(oldItem: ResultXX, newItem: ResultXX): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ResultXX, newItem: ResultXX): Boolean =
            oldItem == newItem
    }

    val differ = AsyncListDiffer(this, diffCallback)

}