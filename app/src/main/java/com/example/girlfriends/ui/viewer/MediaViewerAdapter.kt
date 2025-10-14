package com.example.girlfriends.ui.viewer

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.girlfriends.ui.viewer.model.MediaItem

class MediaViewerAdapter(
    activity: FragmentActivity,
    private val mediaList: List<MediaItem>
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = mediaList.size

    override fun createFragment(position: Int): Fragment {
        return MediaViewerFragment.newInstance(mediaList[position])
    }
}
