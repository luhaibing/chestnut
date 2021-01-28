@file:Suppress("UNUSED_PARAMETER")

package com.mercer.magic.plugin.dependency

import com.android.build.gradle.LibraryExtension
import com.google.gson.Gson
import com.mercer.magic.constant.Dependency
import com.mercer.magic.constant.PluginId
import com.mercer.magic.core.ProjectPlugin
import com.mercer.magic.extension.dependency.DependencyPublish
import com.mercer.magic.extension.dependency.DependencyPublishImpl
import com.mercer.magic.model.dependency.Publication
import com.mercer.magic.model.dependency.SourceSet
import com.mercer.magic.model.dependency.SourceSummary
import com.mercer.magic.model.enum.Category
import com.mercer.magic.util.DATE_FORMAT
import com.mercer.magic.util.writeContentToFile
import org.apache.commons.codec.digest.DigestUtils
import org.gradle.StartParameter
import org.gradle.api.Project
import org.gradle.api.internal.tasks.DefaultSourceSetContainer
import java.io.File

/**
 * @author      ：mercer
 * @date        ：2021-01-27  22:50
 * @description ：本地项目源码发布到本地仓库
 */
class DependencyPublishPlugin : ProjectPlugin() {

    override fun process(project: Project, startParameter: StartParameter) {
        super.process(project, startParameter)
        // 先声明扩展属性接收
        declareExtension(project)
    }

    override fun afterEvaluateProcess(project: Project, startParameter: StartParameter) {
        super.afterEvaluateProcess(project, startParameter)

        // 获取扩展属性
        val extension: DependencyPublish = receiveExtension(project)

        extension.defaultSetting()

        if (!checkStartParameter(startParameter, extension)) {
            return
        }

        val anchor = extension.anchor
            ?: throw NullPointerException("anchor can not be null.")

        anchor.doLast {
            // 保存发布依赖时的资源文件摘要
            keepPublicationJson(project, extension)
        }

    }

    /**
     * 声明扩展属性
     */
    private fun declareExtension(project: Project) {
        project.extensions.create(
            DependencyPublish::class.java,
            Dependency.Extensions.PUBLISH,
            DependencyPublishImpl::class.java,
            project
        )
    }

    /**
     * 检查开始参数
     */
    private fun checkStartParameter(
        startParameter: StartParameter, extension: DependencyPublish
    ): Boolean {
        return true
    }

    /**
     * 接收扩展属性
     */
    private fun receiveExtension(project: Project): DependencyPublish {
        return project.extensions.findByName(Dependency.Extensions.PUBLISH)
                as? DependencyPublish ?: throw NullPointerException(
            "can not receive extensions.findByName(${Dependency.Extensions.PUBLISH})."
        )
    }

    private fun keepPublicationJson(project: Project, extension: DependencyPublish) {

        // 获取 sourceSet
        val pair = obtainSourceSet(project)

        // 获取当前所有文件的摘要内容
        val sourceSummaries = generateSourceSummaries(
            project.projectDir, pair.second.collects(project.projectDir.absolutePath)
        )

        val timestamp = sourceSummaries.maxBy { it.timestamp }?.timestamp ?: 0
        val output = extension.repository.url(project).path

        val publication = Publication(
            extension.repository.notation(project),
            extension.groupId,
            extension.artifactId,
            extension.version,
            File(output, project.name + ".json")
                .absolutePath.substring(project.rootDir.absolutePath.length + 1),
            project.name,
            project.projectDir.absolutePath.substring(project.rootDir.absolutePath.length + 1),
            pair.first,
            DATE_FORMAT.format(timestamp),
            timestamp,
            pair.second,
            sourceSummaries,
        )

        val toJson = Gson().toJson(publication)
        val jsonFile = File(output, "${project.name}.json")
        if (jsonFile.parentFile.exists()) {
            jsonFile.mkdirs()
        }
        if (jsonFile.exists()) {
            jsonFile.createNewFile()
        }

        writeContentToFile(jsonFile, toJson)

    }

    /**
     * 获取模块的资源目录
     */
    private fun obtainSourceSet(
        project: Project
    ): Pair<Category, SourceSet> {
        val pluginManager = project.pluginManager
        return when {
            pluginManager.hasPlugin(PluginId.Android.LIBRARY) -> {
                val container = (project.extensions.findByName(Category.Android.container)
                        as LibraryExtension).sourceSets.findByName(Dependency.SOURCE_CHANNEL)
                    ?: throw NullPointerException("can found ${Category.Android.container}.${Dependency.SOURCE_CHANNEL}")
                Category.Android to SourceSet(project.projectDir, container, project.buildFile)
            }
            pluginManager.hasPlugin(PluginId.Java.JAVA) -> {
                val container = (project.extensions.findByName(Category.Java.container)
                        as DefaultSourceSetContainer).findByName(Dependency.SOURCE_CHANNEL)
                    ?: throw NullPointerException("can found ${Category.Java.container}.${Dependency.SOURCE_CHANNEL}")
                Category.Java to SourceSet(project.projectDir, container, project.buildFile)
            }
            else -> {
                throw NullPointerException("can not found sourceSet.")
            }
        }
    }

    /**
     * 生成资源摘要的集合
     */
    private fun generateSourceSummaries(projectDir: File, values: List<File>): List<SourceSummary> {
        return values
            .filter { it.exists() }
            .map {
                SourceSummary(
                    it.absolutePath.substring(projectDir.absolutePath.length),
                    DigestUtils.md5Hex(String(it.readBytes())),
                    DATE_FORMAT.format(it.lastModified()),
                    it.lastModified(),
                    it.length()
                )
            }
    }


}