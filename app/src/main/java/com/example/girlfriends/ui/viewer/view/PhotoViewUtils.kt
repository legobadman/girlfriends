package com.example.girlfriends.ui.viewer.view

import android.graphics.Matrix
import android.graphics.RectF
import android.widget.ImageView

/**
 * 一些用于计算图片缩放与边界的辅助函数
 */
object PhotoViewUtils {

    fun getDisplayRect(imageView: ImageView, matrix: Matrix): RectF? {
        val drawable = imageView.drawable ?: return null
        val rect = RectF(0f, 0f, drawable.intrinsicWidth.toFloat(), drawable.intrinsicHeight.toFloat())
        matrix.mapRect(rect)
        return rect
    }

    fun getMatrixValues(matrix: Matrix): FloatArray {
        val values = FloatArray(9)
        matrix.getValues(values)
        return values
    }

    fun getScale(matrix: Matrix): Float {
        val values = getMatrixValues(matrix)
        val scaleX = values[Matrix.MSCALE_X]
        val skewY = values[Matrix.MSKEW_Y]
        return kotlin.math.sqrt((scaleX * scaleX + skewY * skewY).toDouble()).toFloat()
    }
}
