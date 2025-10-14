package com.example.girlfriends.ui.viewer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.girlfriends.R
import com.example.girlfriends.ui.viewer.model.MediaItem

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
        // setContentView(R.layout.activity_media_viewer) // 你可以先不设布局，后续加入

        // 读取参数（后续在这里初始化 ViewPager2、工具栏等）
        val items: ArrayList<MediaItem>? =
            intent.getParcelableArrayListExtra(EXTRA_ITEMS)
        val index = intent.getIntExtra(EXTRA_INDEX, 0)

        // TODO: 初始化 ViewPager2，定位到 index；点击切换工具栏显隐；图片/视频分别渲染
    }
}
