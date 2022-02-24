package com.asterisk.movieworld.ui.trailer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.asterisk.movieworld.data.services.tdbmmodel.ResultX
import com.asterisk.movieworld.databinding.TrailerListItemBinding

class TrailerAdapter(
    private val onItemClicked: (ResultX) -> Unit
) : RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrailerViewHolder {
        val layout =
            TrailerListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrailerViewHolder(layout, onClick = { position ->
            val result = differ.currentList[position]
            if (result != null) {
                onItemClicked(result)
            }
        })
    }

    override fun onBindViewHolder(holder: TrailerViewHolder, position: Int) {
        val item = differ.currentList[position]
        if (item != null) {
            holder.bind(item)
        }
    }

    override fun getItemCount() = differ.currentList.size

    inner class TrailerViewHolder(
        private val binding: TrailerListItemBinding,
        private val onClick: (Int) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(resultX: ResultX) {
            binding.apply {
                tvName.text = resultX.name
                tvSite.text = resultX.site
            }
        }

        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val item = differ.currentList[position]
                        if (item != null) {
                            onClick(position)
                        }
                    }
                }
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