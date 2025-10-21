package com.example.girlfriends.data

import android.text.format.Formatter.formatFileSize
import com.example.girlfriends.ui.file.FileItem
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.DecimalFormat

object RemoteFileRepository {

    fun fetchDirectory(url: String): List<FileItem> {
        val conn = URL(url).openConnection() as HttpURLConnection
        conn.requestMethod = "GET"
        conn.connectTimeout = 0  // 先不设超时
        conn.readTimeout = 0

        if (conn.responseCode != 200) {
            conn.disconnect()
            throw RuntimeException("HTTP ${conn.responseCode} ${conn.responseMessage}")
        }

        val jsonText = conn.inputStream.bufferedReader().use(BufferedReader::readText)
        conn.disconnect()

        val root = JSONObject(jsonText)
        val files = root.getJSONArray("files")
        val list = mutableListOf<FileItem>()

        for (i in 0 until files.length()) {
            val obj = files.getJSONObject(i)
            val name = obj.getString("name")
            val isDir = obj.getBoolean("isDirectory")
            val extension = obj.optString("extension", null)
            val modified = obj.optString("modified", null)
            val size = obj.opt("size")?.let { formatFileSize(it.toString()) }

            val itemUrl = if (url.endsWith("/")) "$url$name" else "$url/$name"

            list.add(
                FileItem(
                    name = name,
                    url = itemUrl,
                    isDirectory = isDir,
                    modifiedDate = modified,
                    size = size,
                    extension = extension?.removePrefix(".")
                )
            )
        }

        // 排序：目录在前，文件在后
        return list.sortedWith(compareBy<FileItem> { !it.isDirectory }.thenBy { it.name.lowercase() })
    }

    // 简单的文件大小格式化
    private fun formatFileSize(sizeStr: String): String {
        return try {
            val size = sizeStr.toLong()
            if (size < 1024) "$size B"
            else if (size < 1024 * 1024) "${DecimalFormat("#.##").format(size / 1024.0)} KB"
            else "${DecimalFormat("#.##").format(size / 1024.0 / 1024.0)} MB"
        } catch (_: Exception) {
            sizeStr
        }
    }
}
