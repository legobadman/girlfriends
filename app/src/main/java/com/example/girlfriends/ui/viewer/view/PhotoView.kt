package com.example.girlfriends.ui.viewer.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class PhotoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs) {

    private val attacher = PhotoViewAttacher(this)

    init {
        scaleType = ScaleType.MATRIX
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        attacher.update()
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        attacher.update()
    }

    override fun setScaleType(scaleType: ScaleType) {
        // Always keep matrix
        super.setScaleType(ScaleType.MATRIX)
    }

    fun setZoomable(enabled: Boolean) {
        attacher.isZoomable = enabled
    }
}
