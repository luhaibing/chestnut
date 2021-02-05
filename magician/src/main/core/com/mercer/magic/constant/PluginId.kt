
package com.mercer.magic.constant

/**
 * @author          : mercer
 * @date            : 2021-01-28  16:43
 * @canonicalName   : com.mercer.magic.constant.PluginId
 * @description     : 插件ID常量
 */
@Suppress("SpellCheckingInspection")
object PluginId {

    object Android {
        const val APPLICATION = "com.android.application"
        const val LIBRARY = "com.android.library"
    }

    object Java {
        const val JAVA_LIBRARY = "java-library"
        const val JAVA = "java"
    }

    object Kotlin {
        const val KOTLIN = "kotlin"
        const val KOTLIN_ANDROID = "kotlin-android"
    }

    val ANDROIDS: List<String> by lazy {
        val values = arrayListOf<String>()
        values.add(Android.APPLICATION)
        values.add(Android.LIBRARY)
        values
    }

    val JAVAS: List<String> by lazy {
        val values = arrayListOf<String>()
        values.add(Java.JAVA_LIBRARY)
        values.add(Java.JAVA)
        values
    }

    val KOTLINS: List<String> by lazy {
        val values = arrayListOf<String>()
        values.add(Kotlin.KOTLIN)
        values.add(Kotlin.KOTLIN_ANDROID)
        values
    }

}