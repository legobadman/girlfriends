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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class FileBrowserFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var pathTextView: TextView
    private lateinit var adapter: FileGridAdapter
    private var currentUrl: String = "http://192.168.1.10/media/"  // 初始模拟URL
    private val folderStack = ArrayDeque<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_file_browser, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        pathTextView = view.findViewById(R.id.pathTextView)

        // ✅ 改：不再传入初始 items，用 ListAdapter
        adapter = FileGridAdapter { item ->
            if (item.isDirectory) {
                loadFiles(item.url)
            } else if (item.extension?.lowercase() in listOf("jpg", "jpeg", "png", "gif", "bmp", "webp")) {
                openImageViewer(item)
            }
        }


        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView.adapter = adapter

        loadFiles(currentUrl)

        // 处理返回键
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (folderStack.isNotEmpty()) {
                        currentUrl = folderStack.removeLast()
                        loadFiles(currentUrl)
                    } else {
                        isEnabled = false
                        requireActivity().onBackPressed()
                    }
                }
            })

        return view
    }

    private fun toFileItem(file: File): FileItem {
        val name = file.name
        val isDirectory = file.isDirectory
        val path = file.absolutePath
        val extension = if (isDirectory) "" else file.extension
        return FileItem(name, path, isDirectory, "modifiedData?", "size?", extension)
    }

    private fun navigateToFolder(folder_url: String) {
        folderStack.addLast(currentUrl)
        currentUrl = folder_url
        loadFiles(currentUrl)
    }

    private fun loadFiles(folderUrl: String) {
        pathTextView.text = folderUrl

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val files = RemoteFileRepository.fetchDirectory(folderUrl)
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


    private fun openImageViewer(selectedItem: FileItem) {
        val imageItems = adapter.currentList.filter {
            !it.isDirectory && it.extension?.lowercase() in listOf("jpg", "jpeg", "png", "gif", "bmp", "webp")
        }

        val mediaItems = imageItems.map {
            com.example.girlfriends.ui.viewer.model.MediaItem(
                Uri.parse(it.url),
                com.example.girlfriends.ui.viewer.model.MediaType.IMAGE
            )
        }

        val selectedIndex = imageItems.indexOfFirst { it.url == selectedItem.url }

        com.example.girlfriends.ui.viewer.MediaViewerActivity.start(
            requireContext(),
            mediaItems,
            selectedIndex
        )
    }

}
