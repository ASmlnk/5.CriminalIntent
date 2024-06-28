package com.example.photogallery

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.photogallery.databinding.ListItemGelleryBinding
import com.example.photogallery.model.GalleryItem


class PhotoViewHolder(private val binding: ListItemGelleryBinding):
RecyclerView.ViewHolder(binding.root){

    companion object {
        fun fromInflater(parent: ViewGroup): PhotoViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ListItemGelleryBinding.inflate(inflater, parent, false)
            return PhotoViewHolder(binding)
        }
    }
    fun bind(galleryItem: GalleryItem, onItemClicked: (Uri) -> Unit) {
        binding.itemImageView.load(galleryItem.url) {
            placeholder(R.drawable.ic_launcher_foreground)
        }
        binding.root.setOnClickListener {
            onItemClicked(galleryItem.photoPageUri)
        }

    }
}

class PhotoListAdapter(
    private val galleryItems: List<GalleryItem>,
    private val onItemClicked: (Uri) -> Unit
):
RecyclerView.Adapter<PhotoViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder =
        PhotoViewHolder.fromInflater(parent)

    override fun getItemCount() = galleryItems.size

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val item = galleryItems[position]
        holder.bind(item, onItemClicked)
    }
}