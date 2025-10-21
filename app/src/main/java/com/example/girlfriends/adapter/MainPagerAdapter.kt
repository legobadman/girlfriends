package com.example.girlfriends.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.girlfriends.ui.file.FileBrowserFragment
import com.example.girlfriends.ui.post.PostListFragment

class MainPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                val hostDir = "http://192.168.1.3:80"
                val hostFile = "http://192.168.1.3:8080"
                val rootKey = "韩国妹子"
                FileBrowserFragment.newInstance(hostDir, hostFile, rootKey)
            }
            1 -> {
                val hostDir = "http://192.168.1.3:80"
                val hostFile = "http://192.168.1.3:8080"
                val rootKey = "国产妹子"
                FileBrowserFragment.newInstance(hostDir, hostFile, rootKey)
            }
            else -> PostListFragment()
        }
    }
}
