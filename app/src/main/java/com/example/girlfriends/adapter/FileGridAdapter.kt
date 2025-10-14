package com.example.girlfriends.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.girlfriends.R
import com.example.girlfriends.ui.file.FileItem

class FileGridAdapter(
    private val onClick: (FileItem) -> Unit
) : ListAdapter<FileItem, FileGridAdapter.FileViewHolder>(DIFF) {

    class FileViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivIcon: ImageView = view.findViewById(R.id.ivIcon)
        val tvName: TextView = view.findViewById(R.id.tvName)
        val tvMeta: TextView = view.findViewById(R.id.tvMeta)
    }

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<FileItem>() {
            override fun areItemsTheSame(oldItem: FileItem, newItem: FileItem): Boolean {
                // 用 path 作为唯一键
                return oldItem.path == newItem.path
            }

            override fun areContentsTheSame(oldItem: FileItem, newItem: FileItem): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_file_grid, parent, false)
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val item = getItem(position)

        holder.tvName.text = item.name
        holder.tvMeta.text = listOfNotNull(item.modifiedDate, item.size).joinToString("\n")

        holder.ivIcon.setImageResource(
            when {
                item.isDirectory -> android.R.drawable.ic_menu_agenda
                item.extension?.equals("txt", true) == true -> android.R.drawable.ic_menu_edit
                item.extension?.matches(Regex("jpg|jpeg|png|bmp|webp", RegexOption.IGNORE_CASE)) == true -> android.R.drawable.ic_menu_gallery
                item.extension?.matches(Regex("mp4|mov|avi|mkv", RegexOption.IGNORE_CASE)) == true -> android.R.drawable.ic_media_play
                else -> android.R.drawable.ic_menu_help
            }
        )

        holder.itemView.setOnClickListener { onClick(item) }
    }
}
