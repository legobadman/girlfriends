package com.example.girlfriends.ui.file

data class FileItem(
    val name: String,
    val url: String,          // 拼接完整 HTTP 地址
    val isDirectory: Boolean,
    val modifiedDate: String? = null,
    val size: String? = null,
    val extension: String? = null
)


fun FileItem.isImageFile(): Boolean {
    val lower = name.lowercase()
    return lower.endsWith(".jpg") || lower.endsWith(".jpeg") ||
            lower.endsWith(".png") || lower.endsWith(".gif") ||
            lower.endsWith(".bmp") || lower.endsWith(".webp")
}