package com.asterisk.movieworld.ui.details

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.asterisk.movieworld.R
import com.asterisk.movieworld.data.services.tdbmmodel.Cast
import com.asterisk.movieworld.databinding.MovieCastItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class MovieCastAdapter : RecyclerView.Adapter<MovieCastAdapter.CastViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CastViewHolder {
        val layout =
            MovieCastItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CastViewHolder(layout)
    }

    override fun onBindViewHolder(holder: CastViewHolder, position: Int) {
        val castItem = differ.currentList[position]
        if (castItem != null) {
            holder.bind(castItem)
        }
    }

    override fun getItemCount() = differ.currentList.size

    inner class CastViewHolder(private val binding: MovieCastItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(cast: Cast) {
            val context = binding.root.context
            binding.apply {
                Glide.with(itemView)
                    .load("https://image.tmdb.org/t/p/w342/${cast.profilePath}")
                    .error(ColorDrawable(Color.RED))
                    .centerCrop()
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .into(ivImageTvShow)

                tvName.text = context.getString(R.string.name, cast.name)
                tvCharacter.text = context.getString(R.string.character, cast.character)
            }
        }
    }


    private val diffCallback = object : DiffUtil.ItemCallback<Cast>() {
        override fun areItemsTheSame(oldItem: Cast, newItem: Cast) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Cast, newItem: Cast) =
            oldItem == newItem

    }

    val differ = AsyncListDiffer(this, diffCallback)
}