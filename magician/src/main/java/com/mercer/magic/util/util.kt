@file:Suppress("SimpleDateFormat", "WeekBasedYear", "unused")

package com.mercer.magic.util

import com.google.gson.Gson
import com.mercer.magic.model.RepositoryModel
import java.io.File
import java.text.SimpleDateFormat

/**
 * @author      ：mercer
 * @date        ：2021-01-28  20:52
 * @description ：
 */
inline fun <reified T> Any.asType(): T {
    return Gson().let {
        it.fromJson(it.toJson(this), T::class.java)
    }
}

fun Any.asRepository(): RepositoryModel = asType()


val DATE_FORMAT by lazy {
    SimpleDateFormat("YYYY-MM-DD hh:mm:ss")
}


/**
 * 展开多个文件夹路径获取文件路径
 */
fun unfoldTraverseFolders(parent: String, vararg values: String): List<File> {
    return unfoldTraverseFolders(parent, values.toList())
}

fun unfoldTraverseFolders(parent: String, values: List<String>): List<File> {
    return unfoldTraverseFolders(values.map { File(parent, it) })
}

fun unfoldTraverseFolders(vararg values: File): List<File> {
    return unfoldTraverseFolders(values.toList())
}

fun unfoldTraverseFolders(values: List<File>): List<File> {
    return values.toList()
        .filter { it.exists() }
        .map { unfoldTraverseFolder(it) }
        .flatMap { it.map { f -> f } }
}

/**
 * 遍历展开文件夹
 */
fun unfoldTraverseFolder(src: File): List<File> {
    val values = arrayListOf<File>()

    if (!src.exists()) {
        return values
    }

    val folders = arrayListOf<File>()
    folders.add(src)
    var index = 0
    var folder: File
    var hasNext: Boolean = index < folders.size
    while (hasNext) {
        folder = folders[index++]
        if (!folder.exists()) {
            continue
        }
        val files = folder.listFiles() ?: continue
        for (file in files) {
            if (file.isDirectory) {
                folders.add(file)
                continue
            }
            values.add(file)
        }
        hasNext = index < folders.size
    }
    return values
}

fun <I, O> asCollection(
    collection: Collection<I>, values: MutableCollection<O>, action: (I) -> O
): MutableCollection<O> {
    collection.onEach {
        values.add(action(it))
    }
    return values
}


fun writeContentToFile(file: File, content: String) {
    writeContentToFile(file, content.toByteArray())
}

fun writeContentToFile(file: File, content: ByteArray) {
    if (file.exists()) {
        file.delete()
    }
    if (!file.parentFile.exists()) {
        file.parentFile.mkdirs()
    }
    file.createNewFile()
    val outputStream = file.outputStream()
    outputStream.write(content)
    outputStream.flush()
    outputStream.close()
}