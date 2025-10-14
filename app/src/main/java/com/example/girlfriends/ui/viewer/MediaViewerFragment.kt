package com.example.girlfriends.ui.viewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.girlfriends.R
import com.example.girlfriends.ui.viewer.model.MediaItem

class MediaViewerFragment : Fragment() {

    private lateinit var photoView: ImageView
    private lateinit var progressBar: ProgressBar
    private var mediaItem: MediaItem? = null
    private var isToolbarVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaItem = arguments?.getParcelable("media_item")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_media_viewer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        photoView = view.findViewById(R.id.photoView)
        progressBar = view.findViewById(R.id.loading)

        mediaItem?.let { item ->
            progressBar.visibility = View.VISIBLE

            Glide.with(this)
                .load(item.uri)
                .into(photoView)

            progressBar.visibility = View.GONE
        }

        photoView.setOnClickListener {
            isToolbarVisible = !isToolbarVisible
            (activity as? MediaPagerActivity)?.toggleToolbar(isToolbarVisible)
        }
    }

    companion object {
        fun newInstance(mediaItem: MediaItem): MediaViewerFragment {
            val fragment = MediaViewerFragment()
            val args = Bundle()
            args.putParcelable("media_item", mediaItem)
            fragment.arguments = args
            return fragment
        }
    }
}
