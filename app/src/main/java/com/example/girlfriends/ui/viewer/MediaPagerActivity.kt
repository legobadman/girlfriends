package com.example.girlfriends.ui.viewer

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.girlfriends.R
import com.example.girlfriends.ui.viewer.adapter.MediaPagerAdapter
import com.example.girlfriends.ui.viewer.model.MediaItem

class MediaPagerActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var adapter: MediaPagerAdapter
    private lateinit var bottomBar: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_pager)

        bottomBar = findViewById(R.id.bottomBar)
        viewPager = findViewById(R.id.mediaPager)

        val mediaList =
            intent.getParcelableArrayListExtra<MediaItem>("media_list") ?: emptyList()
        val startIndex = intent.getIntExtra("start_index", 0)

        adapter = MediaPagerAdapter(this, mediaList)
        viewPager.adapter = adapter
        viewPager.setCurrentItem(startIndex, false)

        findViewById<ImageButton>(R.id.btnFavorite).setOnClickListener {
            // TODO: 收藏操作
        }

        findViewById<ImageButton>(R.id.btnTag).setOnClickListener {
            // TODO: 打标签操作
        }

        findViewById<ImageButton>(R.id.btnMore).setOnClickListener {
            // TODO: 弹出菜单操作
        }
    }

    fun toggleToolbar(visible: Boolean) {
        bottomBar.visibility = if (visible) View.VISIBLE else View.GONE
    }
}
