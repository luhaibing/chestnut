package com.mercer.magic.extension

import com.mercer.magic.constant.DependencyExt
import com.mercer.magic.service.OnAcquireListener
import com.mercer.magic.trigger.OnAnchorTrigger
import com.mercer.magic.trigger.OnRepositoryProvider
import com.mercer.magic.trigger.repository.MavenRepositoryProvider
import org.gradle.api.Project
import java.io.File

/**
 * @author          : mercer
 * @date            : 2021-02-06  20:09
 * @canonicalName   : com.mercer.magic.extension.DefaultDependencyPublish
 * @description     :
 */
open class DefaultDependencyPublish(private val target: Project) : DependencyPublish,
    OnAcquireListener<DependencyPublish> {

    override var output: File = toFile(DependencyExt.DEFAULT_FOLDER)

    private fun toFile(value: String): File {
        return File(target.rootDir, value)
    }

    override fun output(value: String): File {
        output = toFile(value)
        return output
    }

    override var anchor: OnAnchorTrigger =
        MavenRepositoryProvider(target).apply { prepare(this@DefaultDependencyPublish) }

    override fun anchor(value: OnAnchorTrigger) {
        anchor = value
    }

    fun prepare() {
        (anchor as? OnRepositoryProvider)?.apply()
    }

    override fun acquire(): DependencyPublish = this

}