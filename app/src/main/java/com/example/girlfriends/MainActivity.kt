package com.example.girlfriends

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.girlfriends.adapter.MainPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var tabLayout: TabLayout
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(R.layout.activity_main)
        }
        catch(e: Exception){
            println("发生异常: ${e.message}")
        }
        finally {
            // 可选，始终执行
            println("操作结束")
        }

        tabLayout = findViewById(R.id.tabLayout)
        tabLayout.tabMode = TabLayout.MODE_SCROLLABLE

        viewPager = findViewById(R.id.viewPager)

        // 设置 ViewPager 的适配器
        val adapter = MainPagerAdapter(this)
        viewPager.adapter = adapter

        // Tab 标题
        val tabTitles = listOf("韩国美女", "国产小姐姐", "达盖尔", "AV频道", "爱心收藏", "冲击时刻", "排行榜")

        // 绑定 TabLayout 与 ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = tabTitles[position]
        }.attach()
    }
}
