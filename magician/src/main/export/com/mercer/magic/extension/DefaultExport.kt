@file:Suppress("DEPRECATION")
package com.mercer.magic.extension

import com.mercer.magic.constant.PluginId
import com.mercer.magic.model.DefaultSourceFolder
import com.mercer.magic.model.SourceFolder
import com.mercer.magic.util.androidSourceSet
import com.mercer.magic.util.sourceSet
import org.gradle.api.Project
import java.io.File
import javax.naming.OperationNotSupportedException

/**
 * @author          : mercer
 * @date            : 2021-02-07  21:50
 * @canonicalName   : com.mercer.magic.extension.DefaultExport
 * @desc            :
 */
open class DefaultExport(private val target: Project) : Export {

    private var src: File? = null

    override lateinit var sourceFolder: SourceFolder

    fun prepare() {
        if (src == null) {
            src(Export.DEFAULT_DIR)
        }
    }

    override fun src(value: String) {
        src = File(target.projectDir, "${SourceFolder.DEFAULT_ROOT_DIR}/${value}")
        val style: Int
        val pluginManager = target.pluginManager
        style = when {
            pluginManager.hasPlugin(PluginId.Android.LIBRARY) &&
                    pluginManager.hasPlugin(PluginId.Kotlin.KOTLIN_ANDROID) -> {
                SourceFolder.FLAG_ANDROID or SourceFolder.FLAG_KOTLIN
            }
            pluginManager.hasPlugin(PluginId.Android.LIBRARY) -> {
                SourceFolder.FLAG_ANDROID
            }
            (pluginManager.hasPlugin(PluginId.Java.JAVA) || pluginManager.hasPlugin(PluginId.Java.JAVA_LIBRARY)) &&
                    pluginManager.hasPlugin(PluginId.Kotlin.KOTLIN) -> {
                SourceFolder.FLAG_JAVA or SourceFolder.FLAG_KOTLIN
            }
            pluginManager.hasPlugin(PluginId.Java.JAVA) || pluginManager.hasPlugin(PluginId.Kotlin.KOTLIN) -> {
                SourceFolder.FLAG_JAVA
            }
            else -> {
                throw OperationNotSupportedException()
            }
        }
        val generate = false
        val coreFolders: MutableList<File> = arrayListOf()
        val resFolders: MutableList<File> = arrayListOf()
        val aidlFolders: MutableList<File> = arrayListOf()
        val assetsFolders: MutableList<File> = arrayListOf()
        val jniFolders: MutableList<File> = arrayListOf()
        val manifest: File?
        val buildFile = if (File(src, SourceFolder.BUILD_FILE).exists()) {
            File(src, SourceFolder.BUILD_FILE)
        } else {
            null
        }
        if (style and SourceFolder.FLAG_ANDROID != 0) {
            val androidSourceSet = androidSourceSet(target)
            coreFolders.addAll(androidSourceSet.java.srcDirs)
            resFolders.addAll(androidSourceSet.res.srcDirs)
            manifest = androidSourceSet.manifest.srcFile
            aidlFolders.addAll(androidSourceSet.aidl.srcDirs)
            assetsFolders.addAll(androidSourceSet.assets.srcDirs)
            jniFolders.addAll(androidSourceSet.jni.srcDirs)
        } else {
            val sourceSet = sourceSet(target)
            coreFolders.addAll(sourceSet.java.srcDirs)
            resFolders.addAll(sourceSet.resources.srcDirs)
            manifest = null
        }
        sourceFolder = DefaultSourceFolder(
            style, generate, src!!, value, coreFolders, resFolders,
            aidlFolders, assetsFolders, jniFolders, manifest, buildFile
        )
    }

}