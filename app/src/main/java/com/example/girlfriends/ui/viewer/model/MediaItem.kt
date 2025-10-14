package com.example.girlfriends.ui.viewer.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaItem(
    val uri: Uri,
    val type: MediaType
) : Parcelable
