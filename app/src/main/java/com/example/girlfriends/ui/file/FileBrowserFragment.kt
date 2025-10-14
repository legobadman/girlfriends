package com.example.girlfriends.ui.file

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.girlfriends.R
import com.example.girlfriends.adapter.FileGridAdapter
import com.example.girlfriends.ui.viewer.MediaViewerActivity
import com.example.girlfriends.ui.viewer.model.MediaItem
import java.io.File

class FileBrowserFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var pathTextView: TextView
    private lateinit var adapter: FileGridAdapter
    private val folderStack = ArrayDeque<File>()
    private var currentFolder: File = File("/") // 起始路径

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_file_browser, container, false)

        recyclerView = view.findViewById(R.id.recyclerView)
        pathTextView = view.findViewById(R.id.pathTextView)

        // ✅ 改：不再传入初始 items，用 ListAdapter
        adapter = FileGridAdapter { item ->
            val file = File(item.path ?: return@FileGridAdapter)
            if (file.isDirectory) {
                navigateToFolder(file)
            } else if (item.extension?.lowercase() in listOf("jpg", "jpeg", "png", "gif", "bmp", "webp")) {
                openImageViewer(file)
            }
        }

        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)
        recyclerView.adapter = adapter

        loadFiles(currentFolder)

        // 处理返回键
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (folderStack.isNotEmpty()) {
                        currentFolder = folderStack.removeLast()
                        loadFiles(currentFolder)
                    } else {
                        // 根目录，不再处理，交给系统
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

    private fun navigateToFolder(folder: File) {
        folderStack.addLast(currentFolder)
        currentFolder = folder
        loadFiles(currentFolder)
    }

    private fun loadFiles(folder: File) {
        pathTextView.text = folder.absolutePath
        val files = folder.listFiles()?.sortedWith(
            compareBy<File> { !it.isDirectory }.thenBy { it.name.lowercase() }
        ) ?: emptyList()

        // ✅ 改：映射为 FileItem 再 submitList
        val fileItems = files.map(::toFileItem)
        adapter.submitList(fileItems)
    }

    private fun openImageViewer(selectedFile: File) {
        val imageFiles = currentFolder.listFiles()?.filter {
            it.isFile && it.extension.lowercase() in listOf("jpg", "jpeg", "png", "gif", "bmp", "webp")
        } ?: return

        // ✅ 改：MediaItem 使用 Uri
        val mediaItems = imageFiles.map {
            val uri = android.net.Uri.fromFile(it) // 如需更安全可改 FileProvider
            com.example.girlfriends.ui.viewer.model.MediaItem(uri, com.example.girlfriends.ui.viewer.model.MediaType.IMAGE)
        }

        val selectedIndex = imageFiles.indexOfFirst { it.absolutePath == selectedFile.absolutePath }

        com.example.girlfriends.ui.viewer.MediaViewerActivity.start(
            requireContext(),
            mediaItems,
            selectedIndex
        )
    }
}
