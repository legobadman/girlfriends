package com.example.girlfriends.ui.viewer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.media3.common.MediaItem as ExoMediaItem
import androidx.media3.exoplayer.ExoPlayer

import androidx.media3.ui.PlayerView //找不到StylePlayerView，只能先搁置

import com.example.girlfriends.R
import com.example.girlfriends.ui.viewer.model.MediaItem

class VideoViewerFragment : Fragment() {

    private lateinit var player: ExoPlayer
    private lateinit var playerView: PlayerView
    private var mediaItem: MediaItem? = null
    private var controlsVisible = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mediaItem = arguments?.getParcelable("media_item")
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_video_viewer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        playerView = view.findViewById(R.id.videoView)

        initializePlayer()

        playerView.setOnClickListener {
            controlsVisible = !controlsVisible
//            playerView.showController()
            if (!controlsVisible) {
//                playerView.hideController()
            }
        }
    }

    private fun initializePlayer() {
        player = ExoPlayer.Builder(requireContext()).build()
        playerView.player = player

        mediaItem?.let {
            val mediaSource = ExoMediaItem.fromUri(it.uri)
            player.setMediaItem(mediaSource)
            player.prepare()
            player.playWhenReady = true
        }
    }

    override fun onStop() {
        super.onStop()
        player.release()
    }

    companion object {
        fun newInstance(item: MediaItem): VideoViewerFragment {
            val fragment = VideoViewerFragment()
            val args = Bundle()
            args.putParcelable("media_item", item)
            fragment.arguments = args
            return fragment
        }
    }
}
