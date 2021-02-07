package com.mercer.magic

import com.mercer.magic.extension.DefaultFixProjectDir
import com.mercer.magic.extension.FixProjectDir
import org.gradle.StartParameter
import org.gradle.api.initialization.Settings
import java.io.File

/**
 * @author          : mercer
 * @date            : 2021-02-05  22:54
 * @canonicalName   : com.mercer.magic.FixProjectDirPlugin
 * @description     : 修正子模块源路径
 */
class FixProjectDirPlugin : FoundationPlugin<Settings, FixProjectDir>() {

    override fun declareExtension(target: Settings) {
        target.extensions.create(
            FixProjectDir::class.java, FixProjectDir.KEY, DefaultFixProjectDir::class.java
        )
    }

    override fun receiveExtension(target: Settings): FixProjectDir {
        return target.extensions.findByName(FixProjectDir.KEY) as? FixProjectDir
            ?: throw NullPointerException("can not receive extensions findByName(${FixProjectDir.KEY}).")
    }

    override fun afterEvaluateHandle(
        target: Settings, extension: FixProjectDir?, startParameter: StartParameter
    ) {
        super.afterEvaluateHandle(target, extension, startParameter)
        val children = target.rootProject.children
        if (extension == null || extension.folders.isEmpty() || children.isEmpty()) {
            return
        }
        children
            .asSequence()
            .filter { descriptor ->
                !descriptor.projectDir.exists()
            }
            .onEach {
                val rootDir = target.rootDir.absolutePath
                val folder = extension.folders.find { f ->
                    File(rootDir + File.separator + f, it.name).exists()
                }
                if (folder != null) {
                    val file = File(rootDir + File.separator + folder, it.name)
                    it.projectDir = file
                } else {
                    // 输出警告
                    logger.error("project(:${it.name}).projectDir is not exists.")
                }
            }
            .toList()
    }

}