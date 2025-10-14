package com.example.girlfriends.data

import com.example.girlfriends.ui.file.FileItem
import org.jsoup.Jsoup

object RemoteFileRepository {

    fun fetchDirectory(url: String): List<FileItem> {
        // 模拟局域网目录，但图片链接是真实互联网资源
        return when (url) {
            "http://192.168.1.10/media/" -> listOf(
                FileItem(
                    name = "Sample Images",
                    url = "http://192.168.1.10/media/images/",
                    isDirectory = true
                )
            )

            "http://192.168.1.10/media/images/" -> listOf(
                FileItem(
                    name = "Mountains.jpg",
                    url = "https://images.unsplash.com/photo-1501785888041-af3ef285b470?w=1600",
                    isDirectory = false,
                    size = "2.3 MB",
                    extension = "jpg"
                ),
                FileItem(
                    name = "Beach.jpg",
                    url = "https://images.unsplash.com/photo-1507525428034-b723cf961d3e?w=1600",
                    isDirectory = false,
                    size = "2.1 MB",
                    extension = "jpg"
                ),
                FileItem(
                    name = "City.jpg",
                    url = "https://images.unsplash.com/photo-1493612276216-ee3925520721?w=1600",
                    isDirectory = false,
                    size = "2.0 MB",
                    extension = "jpg"
                )
            )

            else -> emptyList()
        }
    }

    fun fetchDirectory3(url: String): List<FileItem> {
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
