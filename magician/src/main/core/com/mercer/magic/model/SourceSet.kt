@file:Suppress("DEPRECATION")

package com.mercer.magic.model

import com.android.build.gradle.api.AndroidSourceSet
import com.mercer.magic.constant.PluginId
import com.mercer.magic.enum.Category
import com.mercer.magic.extension.Micro
import com.mercer.magic.util.asCollection
import com.mercer.magic.util.unfoldTraverseFolders
import org.gradle.api.Project
import java.io.File

/**
 * @author          : mercer
 * @date            : 2021-02-06  22:04
 * @canonicalName   : com.mercer.magic.model.SourceSet
 * @description     : 编译文件的目录
 */
@Suppress("DEPRECATION")
data class SourceSet(
    val category: Category,
    val java: List<String>,
    val resource: List<String>,
    val assets: List<String>,
    val jni: List<String>,
    val aidl: List<String>,
    val buildFile: List<String>,
    val manifest: List<String>,
) {

    companion object {

        private fun transformFiles(parent: String, src: List<File>): List<String> {
            return asCollection(src, arrayListOf()) {
                // 裁掉文件绝对路径的父路径
                it.absolutePath.substring(parent.length)
            }.toList()
        }

        fun android(project: Project, container: AndroidSourceSet): SourceSet {
            val path: String = project.projectDir.absolutePath

            val category = Category.Android
            val java: List<String> = transformFiles(path, container.java.srcDirs.toList())
            val resource: List<String> = transformFiles(path, container.res.srcDirs.toList())
            val assets: List<String> = transformFiles(path, container.assets.srcDirs.toList())
            val jni: List<String> = transformFiles(path, container.jni.srcDirs.toList())
            val aidl: List<String> = transformFiles(path, container.aidl.srcDirs.toList())

            val buildFile: MutableList<String> = arrayListOf()
            val manifest: MutableList<String> = arrayListOf()

            buildFile.add(project.buildFile.absolutePath.substring(path.length))
            manifest.add(container.manifest.srcFile.absolutePath.substring(path.length))

            if (project.pluginManager.hasPlugin(PluginId.Custom.MICRO)) {
                val micro = project.extensions.findByName(Micro.KEY) as? Micro
                    ?: throw NullPointerException("can not receive extension findByName(${Micro.KEY})")
                micro.micros.mapNotNull { it.buildFile }.filter { it.exists() }.onEach {
                    buildFile.add(it.absolutePath.substring(path.length))
                }
                micro.micros.mapNotNull { it.manifest }.filter { it.exists() }.onEach {
                    manifest.add(it.absolutePath.substring(path.length))
                }
            }

            return SourceSet(category, java, resource, assets, jni, aidl, buildFile, manifest)
        }

        fun java(project: Project, container: org.gradle.api.tasks.SourceSet): SourceSet {
            val path: String = project.projectDir.absolutePath

            val category = Category.Java
            val java: List<String> = transformFiles(path, container.java.srcDirs.toList())
            val resource: List<String> = transformFiles(path, container.resources.srcDirs.toList())
            val assets: List<String> = arrayListOf()
            val jni: List<String> = arrayListOf()
            val aidl: List<String> = arrayListOf()

            val buildFile: MutableList<String> = arrayListOf()
            val manifest: MutableList<String> = arrayListOf()


            if (project.pluginManager.hasPlugin(PluginId.Custom.MICRO)) {
                val micro = project.extensions.findByName(Micro.KEY) as? Micro
                    ?: throw NullPointerException("can not receive extension findByName(${Micro.KEY})")

                micro.micros.mapNotNull { it.buildFile }.filter { it.exists() }.onEach {
                    buildFile.add(it.absolutePath.substring(path.length))
                }
            }

            buildFile.add(project.buildFile.absolutePath.substring(path.length))

            return SourceSet(category, java, resource, assets, jni, aidl, buildFile, manifest)
        }

    }

    fun collects(folder: String): List<File> {
        val values = arrayListOf<File>()
        values.addAll(unfoldTraverseFolders(folder, java))
        values.addAll(unfoldTraverseFolders(folder, resource))
        values.addAll(unfoldTraverseFolders(folder, assets))
        values.addAll(unfoldTraverseFolders(folder, aidl))
        values.addAll(unfoldTraverseFolders(folder, jni))

        values.addAll(unfoldTraverseFolders(folder, manifest))
        values.addAll(unfoldTraverseFolders(folder, buildFile))

        return values
    }

}