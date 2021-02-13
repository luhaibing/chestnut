package com.mercer.magic.extension

/**
 * @author          : mercer
 * @date            : 2021-02-05  23:52
 * @canonicalName   : com.mercer.magic.model.DefaultFixProjectDir
 * @description     :
 */
open class DefaultFixProjectDir constructor(
    override val folders: MutableList<String> = arrayListOf()
) : FixProjectDir {

    override fun folder(vararg values: String) {
        folder(values.toList())
    }

    override fun folder(values: List<String>) {
        folders.addAll(values)
    }

    fun prepare() {
    }

}