@file:Suppress("DEPRECATION", "unused")

package com.mercer.magic.model.dependency

import com.android.build.gradle.api.AndroidSourceSet
import com.mercer.magic.util.asCollection
import com.mercer.magic.util.unfoldTraverseFolders
import org.gradle.api.tasks.SourceSet
import java.io.File

/**
 * @author      ：mercer
 * @date        ：2021-01-28  23:02
 * @description ：资源目录
 */
data class SourceSet(
    val java: List<String>,
    val resource: List<String>,
    val assets: List<String>,
    val jni: List<String>,
    val aidl: List<String>,
    val buildFile: String,
    val manifest: String?,
) {
    companion object {
        private fun transformFiles(parent: String, src: List<File>): List<String> {
            return asCollection(src, arrayListOf()) {
                // 裁掉文件绝对路径的父路径
                it.absolutePath.substring(parent.length)
            }.toList()
        }
    }

    constructor(path: String, java: List<File>, resource: List<File>, buildFile: File) : this(
        transformFiles(path, java),
        transformFiles(path, resource),
        transformFiles(path, arrayListOf()),
        transformFiles(path, arrayListOf()),
        transformFiles(path, arrayListOf()),
        buildFile.absolutePath.substring(path.length),
        null
    )

    constructor(parent: File, sourceSet: SourceSet, buildFile: File) : this(
        parent.absolutePath,
        sourceSet.java.srcDirs.toList(),
        sourceSet.resources.srcDirs.toList(),
        buildFile
    )

    constructor(
        path: String,
        java: List<File>,
        resource: List<File>,
        assets: List<File>,
        jni: List<File>,
        aidl: List<File>,
        buildFile: File,
        manifest: File?
    ) : this(
        transformFiles(path, java),
        transformFiles(path, resource),
        transformFiles(path, assets),
        transformFiles(path, jni),
        transformFiles(path, aidl),
        buildFile.absolutePath.substring(path.length),
        manifest?.absolutePath?.substring(path.length),
    )


    constructor(parent: File, sourceSet: AndroidSourceSet, buildFile: File) : this(
        parent.absolutePath,
        sourceSet.java.srcDirs.toList(),
        sourceSet.res.srcDirs.toList(),
        sourceSet.assets.srcDirs.toList(),
        sourceSet.jni.srcDirs.toList(),
        sourceSet.aidl.srcDirs.toList(),
        buildFile,
        sourceSet.manifest.srcFile
    )

    fun collects(folder: String): List<File> {
        val values = arrayListOf<File>()
        values.addAll(unfoldTraverseFolders(folder, java))
        values.addAll(unfoldTraverseFolders(folder, resource))
        values.addAll(unfoldTraverseFolders(folder, assets))
        values.addAll(unfoldTraverseFolders(folder, aidl))
        values.addAll(unfoldTraverseFolders(folder, jni))
        if (manifest != null) {
            values.add(File(folder, manifest))
        }
        values.add(File(folder, buildFile))
        return values
    }

}