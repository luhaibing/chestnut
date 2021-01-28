package com.mercer.magic.extension

import java.io.File

/**
 * @author      ：mercer
 * @date        ：2021-01-28  03:42
 * @description ：修正子模块路径
 */
interface FixProjectDir {

    val folders: List<String>

    fun folder(vararg values: String)

    fun folder(values: List<String>)

}

open class FixProjectDirImpl constructor(
    override val folders: MutableList<String> = arrayListOf()
) : FixProjectDir {

    override fun folder(vararg values: String) {
        folder(values.toList())
    }

    override fun folder(values: List<String>) {
        folders.addAll(values)
    }

}