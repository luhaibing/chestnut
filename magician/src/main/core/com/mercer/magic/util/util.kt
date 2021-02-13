@file:Suppress("SimpleDateFormat", "WeekBasedYear", "unused", "DEPRECATION")

package com.mercer.magic.util

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.api.AndroidSourceSet
import com.google.gson.Gson
import com.mercer.magic.model.RepositoryModel
import org.gradle.api.Project
import org.gradle.api.internal.tasks.DefaultSourceSetContainer
import org.gradle.api.tasks.SourceSet
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
    return values
        .toList()
        .filter {
            it.exists()
        }
        .map {
            unfoldTraverseFolder(it)
        }
        .flatMap { it ->
            it.map { f ->
                f
            }
        }
}

/**
 * 遍历展开文件夹
 */
fun unfoldTraverseFolder(src: File): List<File> {
    val values = arrayListOf<File>()

    if (!src.exists()) {
        return values
    }

    if (src.isFile) {
        values.add(src)
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

fun sourceSet(project: Project): SourceSet {
    val sourceSets = project.extensions.findByName("sourceSets") as? DefaultSourceSetContainer
        ?: throw NullPointerException("can not get extensions.sourceSets.")
    return sourceSets.findByName("main")
        ?: throw NullPointerException("can not get sourceSets by findByName(main).")
}

fun androidSourceSet(project: Project): AndroidSourceSet {
    val android = project.extensions.findByName("android") as? BaseExtension
        ?: throw NullPointerException("can not get extensions.android.")
    return android.sourceSets.findByName("main")
        ?: throw NullPointerException("can not get android.sourceSets by findByName(main).")
}
