package com.example.girlfriends.ui.viewer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.girlfriends.R
import com.example.girlfriends.ui.viewer.model.MediaItem
import android.widget.ImageView
import coil.load


class MediaViewerActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_ITEMS = "extra_media_items"
        private const val EXTRA_INDEX = "extra_selected_index"

        fun start(context: Context, items: List<MediaItem>, selectedIndex: Int) {
            val intent = Intent(context, MediaViewerActivity::class.java).apply {
                putParcelableArrayListExtra(EXTRA_ITEMS, ArrayList(items))
                putExtra(EXTRA_INDEX, selectedIndex)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 创建一个全屏 ImageView（不用布局文件）
        val imageView = ImageView(this).apply {
            adjustViewBounds = true
            scaleType = ImageView.ScaleType.FIT_CENTER
        }
        setContentView(imageView)

        val items: ArrayList<MediaItem>? =
            intent.getParcelableArrayListExtra(EXTRA_ITEMS)
        val index = intent.getIntExtra(EXTRA_INDEX, 0)

        val item = items?.getOrNull(index)
        if (item != null) {
            imageView.load(item.uri) {
                crossfade(true)
                listener(
                    onError = { _, _ ->
                        imageView.setImageResource(android.R.drawable.ic_delete)
                    }
                )
            }
        }
        // TODO: 初始化 ViewPager2，定位到 index；点击切换工具栏显隐；图片/视频分别渲染
    }
}
