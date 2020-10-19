package com.peter.memo3.gallery

import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ListAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.peter.memo3.R
import com.peter.memo3.databinding.ItemGalleryBinding
import com.peter.memo3.gallery.data.ImageData

class GalleryAdapter(private val listener: GalleryItemListener) : androidx.recyclerview.widget.ListAdapter<ImageData, GalleryItemHolder>(
    object : DiffUtil.ItemCallback<ImageData>() {
        override fun areItemsTheSame(oldItem: ImageData, newItem: ImageData): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ImageData, newItem: ImageData): Boolean =
            oldItem.hashCode() == newItem.hashCode()
    }
) {
    interface GalleryItemListener {
        fun onClick(position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryItemHolder =
        GalleryItemHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.item_gallery,
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: GalleryItemHolder, position: Int) {
        holder.apply {
            showImage(getItem(position).id)
            setListener(listener)
        }
    }
}
class GalleryItemHolder(private val binding: ItemGalleryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun showImage(id: Long) {
        val imageUrl = Uri.withAppendedPath(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            id.toString()
        )

        Glide.with(binding.root)
            .load(imageUrl)
            .into(binding.image)
    }

    fun setListener(listener: GalleryAdapter.GalleryItemListener) {
        binding.container.setOnClickListener {
            if (adapterPosition != RecyclerView.NO_POSITION) {
                listener.onClick(adapterPosition)
            }
        }
    }
}