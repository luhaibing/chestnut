package com.mercer.magic.extension

import com.mercer.magic.enum.Category
import com.mercer.magic.model.SourceFolder
import java.io.File

/**
 * @author          : mercer
 * @date            : 2021-02-06  00:18
 * @canonicalName   : com.mercer.magic.model.Micro
 * @description     : 微模块插件的扩展属性
 */
interface Micro {

    companion object {

        const val KEY = "micro"

    }

    /**
     * 不存在目录时,自动生成文件夹
     */
    val generate: Boolean

    val rootDir: File

    fun rootDir(value: String)

    fun include(vararg values: String)

    fun include(values: List<String>)

    val micros: List<SourceFolder>

    val category: Category?

}