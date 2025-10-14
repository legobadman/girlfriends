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
            0 -> FileBrowserFragment()
            else -> PostListFragment()
        }
    }
}