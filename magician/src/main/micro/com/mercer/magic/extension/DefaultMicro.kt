package com.mercer.magic.extension

import com.mercer.magic.constant.PluginId
import com.mercer.magic.enum.Category
import com.mercer.magic.model.DefaultSourceFolder
import com.mercer.magic.model.SourceFolder
import org.gradle.api.Project
import org.gradle.api.plugins.PluginManager
import java.io.File

/**
 * @author          : mercer
 * @date            : 2021-02-06  00:19
 * @canonicalName   : com.mercer.magic.model.DefaultMicro
 * @description     :
 */
@Suppress("unused")
open class DefaultMicro(private val target: Project) : Micro {

    private val includes: MutableList<String> = arrayListOf()

    override val micros: MutableList<SourceFolder> = arrayListOf()

    override val category: Category by lazy {
        val pluginManager: PluginManager = target.pluginManager
        when {
            hasPlugins(pluginManager, PluginId.ANDROIDS) -> {
                Category.Android
            }
            hasPlugins(pluginManager, PluginId.JAVAS) -> {
                Category.Java
            }
            else -> {
                // 只处理 java 和 android 类型的模块
                Category.UNKNOWN
            }
        }
    }

    fun prepare() {}

    override var generate: Boolean = false

    override var rootDir: File = File(target.projectDir, SourceFolder.DEFAULT_ROOT_DIR)

    override fun rootDir(value: String) {
        rootDir = File(target.projectDir, value)
    }

    override fun include(vararg values: String) {
        include(values.toList())
    }

    override fun include(values: List<String>) {
        values
            .asSequence()
            .map {
                trimName(it)
            }
            .filter {
                !includes.contains(it)
            }
            .map {
                // File(target.projectDir, "${Micro.DEFAULT_ROOT_DIR}/$it")
                File(rootDir, it)
            }
            .mapNotNull {
                toMicroModule(it)
            }
            .onEach {
                micros.add(it)
            }
            .toList()
    }

    private fun trimName(value: String): String {
        return if (value.startsWith(":")) {
            value.substring(":".length)
        } else {
            value
        }
    }

    private fun toMicroModule(file: File): SourceFolder? {
        return when (category) {
            Category.Android -> {
                val flag = if (target.pluginManager.hasPlugin(PluginId.Kotlin.KOTLIN_ANDROID)) {
                    SourceFolder.FLAG_ANDROID or SourceFolder.FLAG_KOTLIN
                } else {
                    SourceFolder.FLAG_ANDROID
                }
                DefaultSourceFolder(flag, generate, file, file.name)
            }
            Category.Java -> {
                val flag = if (target.pluginManager.hasPlugin(PluginId.Kotlin.KOTLIN)) {
                    SourceFolder.FLAG_JAVA or SourceFolder.FLAG_KOTLIN
                } else {
                    SourceFolder.FLAG_JAVA
                }
                DefaultSourceFolder(flag, generate, file, file.name)
            }
            else -> {
                return null
            }
        }
    }

    private fun cores(folder: File): MutableList<File> {
        val values = arrayListOf<File>()
        values.add(File(folder, SourceFolder.JAVA))
        values.add(File(folder, SourceFolder.KOTLIN))
        return values
    }

    private fun res(folder: File): File {
        return File(folder, SourceFolder.RES)
    }

    private fun resources(folder: File): File {
        return File(folder, SourceFolder.RESOURCES)
    }

    private fun hasPlugins(pluginManager: PluginManager, values: List<String>): Boolean {
        return values.any { pluginManager.hasPlugin(it) }
    }

    private fun buildFile(folder: File): File? {
        val file = File(folder, SourceFolder.BUILD_FILE)
        return if (file.exists()) {
            file
        } else {
            null
        }
    }

    private fun manifestFile(folder: File): File? {
        val file = File(folder, SourceFolder.MANIFEST)
        return if (file.exists()) {
            file
        } else {
            null
        }
    }

}