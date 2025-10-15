package com.example.girlfriends.ui.viewer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.github.chrisbanes.photoview.PhotoView
import com.example.girlfriends.ui.viewer.model.MediaItem

class MediaViewerActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_ITEMS = "extra_media_items"
        private const val EXTRA_INDEX = "extra_selected_index"

        /**
         * 从 Fragment/Activity 调用：
         * MediaViewerActivity.start(requireActivity(), items, index)
         */
        fun start(context: Context, items: List<MediaItem>, selectedIndex: Int) {
            val intent = Intent(context, MediaViewerActivity::class.java).apply {
                putParcelableArrayListExtra(EXTRA_ITEMS, ArrayList(items))
                putExtra(EXTRA_INDEX, selectedIndex)
            }
            // 如果传入的不是 Activity context（比如 Application），需要加 FLAG_ACTIVITY_NEW_TASK
            if (context !is android.app.Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 使用 PhotoView 支持双击/手势缩放
        val photoView = PhotoView(this).apply {
            minimumScale = 1.0f
            maximumScale = 5.0f
        }
        setContentView(photoView)

        val items: ArrayList<MediaItem>? =
            intent.getParcelableArrayListExtra(EXTRA_ITEMS)
        val index = intent.getIntExtra(EXTRA_INDEX, 0)

        val item = items?.getOrNull(index)
        if (item != null) {
            photoView.load(item.uri) {
                crossfade(true)
                listener(onError = { _, _ ->
                    photoView.setImageResource(android.R.drawable.ic_delete)
                })
            }
        }
    }
}
