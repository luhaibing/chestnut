package com.mercer.magic.model

import org.dom4j.io.OutputFormat
import java.io.File

/**
 * @author          : mercer
 * @date            : 2021-02-07  18:43
 * @canonicalName   : com.mercer.magic.model.SourceFolder
 * @desc            :
 */
interface SourceFolder {

    companion object {

        // value
        const val FLAG_JAVA = 1

        const val FLAG_ANDROID = FLAG_JAVA shl 1

        const val FLAG_KOTLIN = FLAG_ANDROID shl 1


        /////////////////////////////

        const val DEFAULT_ROOT_DIR = "src"

        /////////////////////////////


        const val JAVA = "java"
        const val KOTLIN = "kotlin"
        const val RES = "res"
        const val RESOURCES = "resources"
        const val BUILD_FILE = "build.gradle"

        const val ASSETS = "assets"
        const val JNI = "jni"
        const val AIDL = "aidl"
        const val MANIFEST = "AndroidManifest.xml"

        /////////////////////////////

        // 自定义xml样式
        val OUTPUT_FORMAT = OutputFormat().apply {
            setIndentSize(2)// 行缩进
            isNewlines = true // 一个结点为一行
            isTrimText = true // 去重空格
            isOmitEncoding = false
            isPadText = true
            isNewLineAfterDeclaration = false
            isExpandEmptyElements = false
        }

    }

    val style: Int

    /**
     * 类型
     */
    val name: String

    val coreFolders: List<File>
    fun java(vararg values: File)
    fun kotlin(vararg values: File)

    val resFolders: List<File>
    fun res(vararg values: File)

    val aidlFolders: List<File>
    fun aidl(vararg values: File)

    val assetsFolders: List<File>
    fun assets(vararg values: File)

    val jniFolders: List<File>
    fun jni(vararg values: File)

    val manifest: File?
    fun manifest(value: File)

    val buildFile: File?

    fun buildFile(value: File)

}