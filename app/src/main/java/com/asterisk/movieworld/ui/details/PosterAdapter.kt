package com.asterisk.movieworld.ui.details

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.asterisk.movieworld.data.services.tdbmmodel.Poster
import com.asterisk.movieworld.databinding.MovieImageItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class PosterAdapter(
    private val posters: List<Poster>
) : RecyclerView.Adapter<PosterAdapter.ImageViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val layout =
            MovieImageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(layout)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentItem = posters[position]
        holder.bind(currentItem.filePath)
    }

    override fun getItemCount(): Int {
        return 5
    }

    inner class ImageViewHolder(private val binding: MovieImageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(image: String) {
            binding.apply {
                Glide.with(itemView)
                    .load("https://image.tmdb.org/t/p/w342/$image")
                    .error(ColorDrawable(Color.RED))
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(ivPoster)
            }
        }

    }
}