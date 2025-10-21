package com.example.girlfriends.ui.file

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.girlfriends.R
import com.example.girlfriends.adapter.FileGridAdapter
import com.example.girlfriends.data.RemoteFileRepository
import com.example.girlfriends.ui.viewer.MediaViewerActivity
import com.example.girlfriends.ui.viewer.model.MediaItem
import com.example.girlfriends.ui.viewer.model.MediaType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.ArrayDeque

class FileBrowserFragment : Fragment() {

    private lateinit var hostDir: String     // 目录服务地址，如 http://192.168.3.104:80
    private lateinit var hostFile: String    // 文件服务地址，如 http://192.168.3.104:8080
    private lateinit var rootKey: String     // 关键目录，如 “风景图”
    private var currentPath: String = ""
    private val folderStack = ArrayDeque<String>()

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FileGridAdapter
    private lateinit var pathTextView: TextView

    companion object {
        private const val ARG_HOST_DIR = "arg_host_dir"
        private const val ARG_HOST_FILE = "arg_host_file"
        private const val ARG_ROOT_KEY = "arg_root_key"

        fun newInstance(hostDir: String, hostFile: String, rootKey: String): FileBrowserFragment {
            val fragment = FileBrowserFragment()
            fragment.arguments = Bundle().apply {
                putString(ARG_HOST_DIR, hostDir)
                putString(ARG_HOST_FILE, hostFile)
                putString(ARG_ROOT_KEY, rootKey)
            }
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            hostDir = it.getString(ARG_HOST_DIR) ?: ""
            hostFile = it.getString(ARG_HOST_FILE) ?: ""
            rootKey = it.getString(ARG_ROOT_KEY) ?: ""
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_file_browser, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        pathTextView = view.findViewById(R.id.pathTextView)

        adapter = FileGridAdapter { item ->
            if (item.isDirectory) {
                folderStack.addLast(currentPath)
                currentPath = if (currentPath.isEmpty()) item.name else "$currentPath/${item.name}"
                loadFiles()
            } else if (item.extension?.lowercase() in listOf("jpg", "jpeg", "png", "gif", "bmp", "webp")) {
                openImageViewer(item)
            }
        }

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView.adapter = adapter

        loadFiles() // 首次加载

        // 返回键逻辑
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (folderStack.isNotEmpty()) {
                        currentPath = folderStack.removeLast()
                        loadFiles()
                    } else {
                        isEnabled = false
                        requireActivity().onBackPressed()
                    }
                }
            })

        return view
    }

    /** 拼接目录请求URL */
    private fun buildDirUrl(): String {
        return if (currentPath.isEmpty())
            "$hostDir/$rootKey"
        else
            "$hostDir/$rootKey/$currentPath"
    }

    /** 拼接文件访问URL（用于图片） */
    private fun buildFileUrl(relativePath: String): String {
        return "$hostFile/$rootKey/$relativePath"
    }

    /** 加载目录内容 */
    private fun loadFiles() {
        val fullUrl = buildDirUrl()
        pathTextView.text = fullUrl

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val files = RemoteFileRepository.fetchDirectory(fullUrl)
                withContext(Dispatchers.Main) {
                    adapter.submitList(files)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "加载失败: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    /** 打开图片查看器（使用文件端口访问） */
    private fun openImageViewer(selectedItem: FileItem) {
        val imageItems = adapter.currentList.filter {
            !it.isDirectory && it.extension?.lowercase() in listOf("jpg", "jpeg", "png", "gif", "bmp", "webp")
        }

        val mediaItems = imageItems.map {
            val relative = if (currentPath.isEmpty()) it.name else "$currentPath/${it.name}"
            MediaItem(
                uri = Uri.parse(buildFileUrl(relative)),
                type = MediaType.IMAGE
            )
        }

        val selectedIndex = imageItems.indexOfFirst { it.name == selectedItem.name }

        MediaViewerActivity.start(requireContext(), mediaItems, selectedIndex)
    }
}
