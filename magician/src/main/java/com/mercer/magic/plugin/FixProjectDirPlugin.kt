@file:Suppress("ControlFlowWithEmptyBody")

package com.mercer.magic.plugin

import com.mercer.magic.constant.Fix
import com.mercer.magic.extension.FixProjectDir
import com.mercer.magic.extension.FixProjectDirImpl
import org.gradle.api.Plugin
import org.gradle.api.initialization.ProjectDescriptor
import org.gradle.api.initialization.Settings
import java.io.File

/**
 * @author      ：mercer
 * @date        ：2021-01-27  22:49
 * @description ：修正子模块源路径
 */
class FixProjectDirPlugin : Plugin<Settings> {

    override fun apply(target: Settings) {

        // 先声明扩展属性接收
        declareExtension(target)

        target.gradle.settingsEvaluated {

            // 获取扩展属性
            val extension: FixProjectDir = receiveExtension(target)

            // 真正的处理逻辑
            process(target, extension, target.rootProject.children)

        }

    }

    /**
     * 处理逻辑
     */
    private fun process(
        settings: Settings, extension: FixProjectDir, children: Set<ProjectDescriptor>
    ) {
        if (extension.folders.isEmpty() || children.isEmpty()) {
            return
        }
        children
            .asSequence()
            .filter {descriptor->
                !descriptor.projectDir.exists()
            }
            .onEach {
                val rootDir = settings.rootDir.absolutePath
                val folder = extension.folders.find { f ->
                    File(rootDir + File.separator + f, it.name).exists()
                }
                if (folder != null) {
                    val file = File(rootDir + File.separator + folder, it.name)
                    it.projectDir = file
                } else {
                    // 输出警告
                }
            }
            .toList()
    }

    /**
     * 接收扩展
     */
    private fun receiveExtension(target: Settings): FixProjectDir {
        return target.extensions.findByName(Fix.FIX_PROJECT_DIR) as? FixProjectDir
            ?: throw NullPointerException("can not receive extensions.findByName(${Fix.FIX_PROJECT_DIR}).")
    }

    /**
     * 声明扩展
     */
    private fun declareExtension(target: Settings) {
        target.extensions.create(
            FixProjectDir::class.java,
            Fix.FIX_PROJECT_DIR,
            FixProjectDirImpl::class.java
        )
    }

}