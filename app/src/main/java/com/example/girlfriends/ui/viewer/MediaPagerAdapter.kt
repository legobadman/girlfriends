package com.example.girlfriends.ui.viewer

import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.github.chrisbanes.photoview.PhotoView
import com.example.girlfriends.ui.viewer.model.MediaItem
import android.view.GestureDetector
import android.view.MotionEvent

class MediaPagerAdapter(
    private val items: List<MediaItem>
) : RecyclerView.Adapter<MediaPagerAdapter.ViewHolder>() {

    class ViewHolder(val photoView: PhotoView) : RecyclerView.ViewHolder(photoView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val photoView = PhotoView(parent.context).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT
            )
            minimumScale = 1.0f
            maximumScale = 4.0f
        }

        var bDirectZoomMax = false;
        if (bDirectZoomMax) {
            photoView.setOnDoubleTapListener(object : GestureDetector.SimpleOnGestureListener() {
                override fun onDoubleTap(e: MotionEvent): Boolean {
                    val current = photoView.scale
                    val max = photoView.maximumScale
                    val min = photoView.minimumScale
                    if (current < max * 0.9f) {
                        photoView.setScale(max, e.x, e.y, true)
                    } else {
                        photoView.setScale(min, e.x, e.y, true)
                    }
                    return true
                }

                //（可选）单击可用来切换工具栏显隐
                override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                    return false
                }
            })
        }

        return ViewHolder(photoView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.photoView.load(item.uri) {
            crossfade(true)
            listener(onError = { _, _ ->
                holder.photoView.setImageResource(android.R.drawable.ic_delete)
            })
        }
    }

    override fun getItemCount(): Int = items.size
}
