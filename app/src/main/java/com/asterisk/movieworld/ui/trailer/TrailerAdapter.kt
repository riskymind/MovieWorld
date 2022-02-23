package com.asterisk.movieworld.ui.trailer

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.asterisk.movieworld.data.services.tdbmmodel.ResultX
import com.asterisk.movieworld.databinding.SimilarMovieItemBinding
import com.asterisk.movieworld.databinding.TrailerListItemBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class TrailerAdapter : RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerViewHolder {
        val layout =
            TrailerListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrailerViewHolder(layout)
    }

    override fun onBindViewHolder(holder: TrailerViewHolder, position: Int) {
        val item = differ.currentList[position]
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun getItemCount() = differ.currentList.size

    inner class TrailerViewHolder(private val binding: TrailerListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(resultX: ResultX) {
            binding.apply {
                tvName.text = resultX.name
                tvSite.text = resultX.site
            }
        }
    }

    private val diffCallBack = object : DiffUtil.ItemCallback<ResultX>() {
        override fun areItemsTheSame(oldItem: ResultX, newItem: ResultX) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ResultX, newItem: ResultX) =
            oldItem == newItem
    }

    val differ = AsyncListDiffer(this, diffCallBack)
}