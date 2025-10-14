package com.example.girlfriends.data

import com.example.girlfriends.ui.file.FileItem
import org.jsoup.Jsoup

object RemoteFileRepository {

    fun fetchDirectory(url: String): List<FileItem> {
        // 模拟：根据URL返回不同的“假目录”
        return when (url) {
            "http://192.168.1.10/media/" -> listOf(
                FileItem("Photos", "http://192.168.1.10/media/photos/", true, null, null, null),
                FileItem("Videos", "http://192.168.1.10/media/videos/", true, null, null, null),
                FileItem("Music", "http://192.168.1.10/media/music/", true, null, null, null)
            )

            "http://192.168.1.10/media/photos/" -> listOf(
                FileItem("Beach.jpg", "http://192.168.1.10/media/photos/beach.jpg", false, null, "2.4 MB", "jpg"),
                FileItem("Mountain.png", "http://192.168.1.10/media/photos/mountain.png", false, null, "3.1 MB", "png"),
                FileItem("Family/", "http://192.168.1.10/media/photos/family/", true, null, null, null)
            )

            "http://192.168.1.10/media/photos/family/" -> listOf(
                FileItem("Dad.png", "http://192.168.1.10/media/photos/family/dad.png", false, null, "1.8 MB", "png"),
                FileItem("Mom.png", "http://192.168.1.10/media/photos/family/mom.png", false, null, "1.9 MB", "png")
            )

            "http://192.168.1.10/media/videos/" -> listOf(
                FileItem("Vacation.mp4", "http://192.168.1.10/media/videos/vacation.mp4", false, null, "54 MB", "mp4"),
                FileItem("Party.mov", "http://192.168.1.10/media/videos/party.mov", false, null, "103 MB", "mov")
            )

            else -> emptyList()
        }
    }

    fun fetchDirectory2(url: String): List<FileItem> {
        val doc = Jsoup.connect(url).get()

        // 假设服务器返回一个简单的 HTML 文件列表
        val items = mutableListOf<FileItem>()
        for (element in doc.select("a")) {
            val name = element.text()
            val href = element.attr("href")

            if (href == "../") continue // 忽略返回上级目录

            val isDir = href.endsWith("/")
            val itemUrl = if (url.endsWith("/")) url + href else "$url/$href"

            items.add(
                FileItem(
                    name = name.trimEnd('/'),
                    url = itemUrl,
                    isDirectory = isDir,
                    extension = if (!isDir) name.substringAfterLast('.', "") else null
                )
            )
        }

        return items.sortedWith(
            compareBy<FileItem> { !it.isDirectory }.thenBy { it.name.lowercase() }
        )
    }
}
