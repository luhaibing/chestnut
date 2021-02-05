package com.mercer.magic.extension

/**
 * @author          : mercer
 * @date            : 2021-02-05  23:52
 * @canonicalName   : com.mercer.magic.model.FixProjectDir
 * @description     : 修正子模块的源路径
 */
interface FixProjectDir {

    val folders: List<String>

    fun folder(vararg values: String)

    fun folder(values: List<String>)

    companion object {
        const val KEY = "fix"
    }

}