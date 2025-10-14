package com.example.girlfriends.ui.file

data class FileItem(
    val name: String,
    val path: String?,
    val isDirectory: Boolean,
    val url: String,
    val modifiedDate: String?,  // 例如：2023/06/03
    val size: String?,          // 例如：2.48 KB 或 "2 项"
    val extension: String? = null
)

fun FileItem.isImageFile(): Boolean {
    val lower = name.lowercase()
    return lower.endsWith(".jpg") || lower.endsWith(".jpeg") ||
            lower.endsWith(".png") || lower.endsWith(".gif") ||
            lower.endsWith(".bmp") || lower.endsWith(".webp")
}