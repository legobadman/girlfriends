package com.example.girlfriends.ui.viewer.view

import android.graphics.Matrix
import android.graphics.RectF
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.view.View.OnTouchListener
import kotlin.math.max
import kotlin.math.min

class PhotoViewAttacher(private val imageView: PhotoView) : OnTouchListener {

    private val scaleGestureDetector = ScaleGestureDetector(imageView.context, ScaleListener())
    private val gestureDetector = GestureDetector(imageView.context, GestureListener())

    private val baseMatrix = Matrix()
    private val drawMatrix = Matrix()
    private val displayRect = RectF()

    private var scale = 1f
    private var minScale = 1f
    private var maxScale = 3f

    var isZoomable = true

    init {
        imageView.setOnTouchListener(this)
        update()
    }

    fun update() {
        imageView.drawable?.let { drawable ->
            val viewWidth = imageView.width.toFloat()
            val viewHeight = imageView.height.toFloat()
            val drawableWidth = drawable.intrinsicWidth.toFloat()
            val drawableHeight = drawable.intrinsicHeight.toFloat()

            baseMatrix.reset()

            val scale: Float = min(viewWidth / drawableWidth, viewHeight / drawableHeight)
            val dx = (viewWidth - drawableWidth * scale) * 0.5f
            val dy = (viewHeight - drawableHeight * scale) * 0.5f

            baseMatrix.postScale(scale, scale)
            baseMatrix.postTranslate(dx, dy)

            resetMatrix()
        }
    }

    private fun resetMatrix() {
        drawMatrix.set(baseMatrix)
        imageView.imageMatrix = drawMatrix
    }

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        if (!isZoomable || event == null) return false
        scaleGestureDetector.onTouchEvent(event)
        gestureDetector.onTouchEvent(event)
        return true
    }

    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            val scaleFactor = detector.scaleFactor
            val origScale = scale
            scale *= scaleFactor
            scale = max(minScale, min(scale, maxScale))

            val factor = scale / origScale
            drawMatrix.postScale(factor, factor, detector.focusX, detector.focusY)
            imageView.imageMatrix = drawMatrix
            return true
        }
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(e: MotionEvent): Boolean {
            toggleZoom(e.x, e.y)
            return true
        }
    }

    private fun toggleZoom(focusX: Float, focusY: Float) {
        scale = if (scale > minScale) minScale else maxScale
        update()
        drawMatrix.postScale(scale, scale, focusX, focusY)
        imageView.imageMatrix = drawMatrix
    }
}
