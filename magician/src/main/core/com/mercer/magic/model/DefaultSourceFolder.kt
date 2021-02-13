package com.mercer.magic.model

import org.dom4j.DocumentHelper
import org.dom4j.io.XMLWriter
import java.io.File
import java.io.FileOutputStream

/**
 * @author          : mercer
 * @date            : 2021-02-07  18:44
 * @canonicalName   : com.mercer.magic.model.DefaultSourceFolder
 * @desc            : 资源源目录
 */
@Suppress("unused")
class DefaultSourceFolder(
    override val style: Int,
    private val generate: Boolean,
    folder: File,
    override val name: String,
    override val coreFolders: MutableList<File> = arrayListOf(),
    override val resFolders: MutableList<File> = arrayListOf(),
    override val aidlFolders: MutableList<File> = arrayListOf(),
    override val assetsFolders: MutableList<File> = arrayListOf(),
    override val jniFolders: MutableList<File> = arrayListOf(),
    override var manifest: File? = null,
    override var buildFile: File? = null
) : SourceFolder {

    init {
        defaultSet(folder)
        if (style and SourceFolder.FLAG_JAVA != 0) {
            defaultSetJava(style, folder)
        }
        if (style and SourceFolder.FLAG_ANDROID != 0) {
            defaultSetAndroid(style, folder)
        }
    }

    private fun defaultSet(value: File) {
        if (generate && !value.exists()) {
            value.mkdirs()
        }
    }

    private fun defaultSetJava(style: Int, value: File) {
        java(File(value, SourceFolder.JAVA))
        if (style and SourceFolder.FLAG_KOTLIN != 0) {
            kotlin(File(value, SourceFolder.KOTLIN))
        }
        res(File(value, SourceFolder.RESOURCES))

        if (!value.exists() || value.isFile || value.listFiles().isNullOrEmpty()) {
            return
        }

        var buildFile = value.listFiles()!!.toList().find { it.name == SourceFolder.BUILD_FILE }
        if (generate && buildFile == null) {
            buildFile = generateBuildFile(value)
        }
        buildFile?.let { buildFile(it) }

    }

    private fun defaultSetAndroid(style: Int, value: File) {
        java(File(value, SourceFolder.JAVA))
        if (style and SourceFolder.FLAG_KOTLIN != 0) {
            kotlin(File(value, SourceFolder.KOTLIN))
        }
        res(File(value, SourceFolder.RES))
        assets(File(value, SourceFolder.ASSETS))
        jni(File(value, SourceFolder.JNI))
        aidl(File(value, SourceFolder.AIDL))

        if (!value.exists() || value.isFile || value.listFiles().isNullOrEmpty()) {
            return
        }

        var manifest = value.listFiles()!!.toList().find { it.name == SourceFolder.MANIFEST }
        if (generate && manifest == null) {
            manifest = generateDefaultAndroidManifest(value)
        }
        manifest?.let { manifest(it) }

        var buildFile = value.listFiles()!!.toList().find { it.name == SourceFolder.BUILD_FILE }
        if (generate && buildFile == null) {
            buildFile = generateBuildFile(value)
        }
        buildFile?.let { buildFile(it) }
    }

    private fun generateBuildFile(value: File): File {
        val buildFile = File(value, SourceFolder.BUILD_FILE)
        buildFile.parentFile.mkdirs()
        buildFile.createNewFile()
        val bytes = "dependencies { \r\n }".toByteArray()
        val fileOutputStream = FileOutputStream(buildFile)
        fileOutputStream.write(bytes)
        fileOutputStream.flush()
        fileOutputStream.close()
        return buildFile
    }

    private fun generateDefaultAndroidManifest(value: File): File {
        val manifest = File(value, SourceFolder.MANIFEST)
        manifest.parentFile.mkdirs()
        manifest.createNewFile()
        val document = DocumentHelper.createDocument()
        val manifestElement = document.addElement("manifest")
        manifestElement.addAttribute(
            "xmlns:android",
            "http://schemas.android.com/apk/res/android"
        )
        manifestElement.addAttribute("package", ".${value.name}")
        // 输出xml文件
        val writer = XMLWriter(FileOutputStream(manifest), SourceFolder.OUTPUT_FORMAT)
        writer.write(document)
        return manifest
    }

    override fun kotlin(vararg values: File) {
        java(*values)
    }

    /**
     * 收集
     */
    private fun collect(values: Collection<File>, target: MutableList<File>) {
        values.toList().map {
            if (generate && !it.exists()) {
                it.mkdirs()
            }
            target.add(it)
        }
    }

    override fun java(vararg values: File) {
        // coreFolders.addAll(values)
        collect(values.toList(), coreFolders)
    }

    override fun res(vararg values: File) {
        // resFolders.addAll(values)
        collect(values.toList(), resFolders)
    }

    override fun aidl(vararg values: File) {
        // aidlFolders.addAll(values)
        collect(values.toList(), aidlFolders)
    }

    override fun assets(vararg values: File) {
        // assetsFolders.addAll(values)
        collect(values.toList(), assetsFolders)
    }

    override fun jni(vararg values: File) {
        // jniFolders.addAll(values)
        collect(values.toList(), jniFolders)
    }

    override fun manifest(value: File) {
        manifest = value
    }

    override fun buildFile(value: File) {
        buildFile = value
    }

}