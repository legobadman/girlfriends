package com.example.girlfriends.ui.viewer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import coil.load
import com.github.chrisbanes.photoview.PhotoView
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
            if (context !is android.app.Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_viewer)

        val items: ArrayList<MediaItem>? =
            intent.getParcelableArrayListExtra(EXTRA_ITEMS)
        val index = intent.getIntExtra(EXTRA_INDEX, 0)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        if (items != null) {
            viewPager.adapter = MediaPagerAdapter(items)
            viewPager.setCurrentItem(index, false)
        }
    }
}
