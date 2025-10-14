package com.example.girlfriends.ui.viewer.view

import android.os.Build
import android.view.View
import android.widget.OverScroller
import android.widget.Scroller

/**
 * 用于兼容不同 Android 版本的滚动代理
 */
interface ScrollerProxy {
    fun computeScrollOffset(): Boolean
    fun getCurrX(): Int
    fun getCurrY(): Int
    fun fling(startX: Int, startY: Int, velocityX: Int, velocityY: Int, minX: Int, maxX: Int, minY: Int, maxY: Int)
    fun forceFinished(finished: Boolean)
}

class GingerScroller(context: View) : ScrollerProxy {
    private val scroller = Scroller(context.context)

    override fun computeScrollOffset(): Boolean = scroller.computeScrollOffset()
    override fun getCurrX(): Int = scroller.currX
    override fun getCurrY(): Int = scroller.currY
    override fun fling(startX: Int, startY: Int, velocityX: Int, velocityY: Int, minX: Int, maxX: Int, minY: Int, maxY: Int) {
        scroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY)
    }

    override fun forceFinished(finished: Boolean) {
        scroller.forceFinished(finished)
    }
}

class IcsScroller(context: View) : ScrollerProxy {
    private val scroller = OverScroller(context.context)

    override fun computeScrollOffset(): Boolean = scroller.computeScrollOffset()
    override fun getCurrX(): Int = scroller.currX
    override fun getCurrY(): Int = scroller.currY
    override fun fling(startX: Int, startY: Int, velocityX: Int, velocityY: Int, minX: Int, maxX: Int, minY: Int, maxY: Int) {
        scroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY)
    }

    override fun forceFinished(finished: Boolean) {
        scroller.forceFinished(finished)
    }
}

object ScrollerFactory {
    fun create(context: View): ScrollerProxy {
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD) {
            GingerScroller(context)
        } else {
            IcsScroller(context)
        }
    }
}
