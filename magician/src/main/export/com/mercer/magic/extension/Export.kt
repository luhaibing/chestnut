package com.mercer.magic.extension

import com.mercer.magic.model.SourceFolder
import java.io.File

/**
 * @author          : mercer
 * @date            : 2021-02-06  00:25
 * @canonicalName   : com.mercer.magic.extension.Export
 * @description     : 模块导出插件的扩展属性
 */
interface Export {

    companion object {

        const val KEY = "export"

        const val DEFAULT_DIR = "export"

    }

    fun src(value: String)

    val sourceFolder: SourceFolder

}