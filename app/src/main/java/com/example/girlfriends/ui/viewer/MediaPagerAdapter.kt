package com.example.girlfriends.ui.viewer.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.girlfriends.ui.viewer.MediaViewerFragment
import com.example.girlfriends.ui.viewer.VideoViewerFragment
import com.example.girlfriends.ui.viewer.model.MediaItem
import com.example.girlfriends.ui.viewer.model.MediaType


class MediaPagerAdapter(
    activity: AppCompatActivity,
    private val items: List<MediaItem>
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = items.size

    override fun createFragment(position: Int): Fragment {
        val item = items[position]
        return if (item.type == MediaType.IMAGE) {
            MediaViewerFragment.newInstance(item)
        } else {
            // 暂时使用空 Fragment 或抛出异常
            Fragment() // 或 throw IllegalStateException("Video not supported yet.")
        }
    }
}
