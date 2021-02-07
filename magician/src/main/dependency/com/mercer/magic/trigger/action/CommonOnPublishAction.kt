package com.mercer.magic.trigger.action

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.mercer.magic.trigger.OnPublishAction
import com.mercer.magic.constant.PluginId
import com.mercer.magic.enum.Category
import com.mercer.magic.extension.DependencyPublish
import com.mercer.magic.model.Publication
import com.mercer.magic.model.SourceSet
import com.mercer.magic.model.SourceSummary
import com.mercer.magic.model.UriModel
import com.mercer.magic.util.DATE_FORMAT
import com.mercer.magic.util.writeContentToFile
import org.apache.commons.codec.digest.DigestUtils
import org.gradle.api.Project
import java.io.File
import java.util.*

/**
 * @author          : mercer
 * @date            : 2021-02-06  22:11
 * @canonicalName   : com.mercer.magic.trigger.action.CommonOnOperateAction
 * @description     : 依赖发布动作
 */
abstract class CommonOnPublishAction(override val category: Category) : OnPublishAction {

    override fun sourceSummaries(target: Project, sourceSet: SourceSet): List<SourceSummary> {
        val folder = target.projectDir.absolutePath
        return sourceSet
            .collects(folder)
            .map { fileToSourceSummary(folder, it) }
            .toList()
    }


    open fun fileToSourceSummary(folder: String, file: File): SourceSummary {
        return SourceSummary(
            file.absolutePath.substring(folder.length + 1),
            file.length(),
            DigestUtils.md5Hex(file.readText().trim()).toUpperCase(Locale.ROOT),
            DATE_FORMAT.format(file.lastModified()),
            file.lastModified(),
        )
    }

    override fun splicePublication(
        target: Project,
        extension: DependencyPublish,
        sourceSet: SourceSet,
        sourceSummaries: List<SourceSummary>
    ): Publication {

        val dependencyNotation = extension.anchor.notation(target)
        val split = dependencyNotation.split(":")
        val timestamp = sourceSummaries.maxBy { it.timestamp }?.timestamp ?: 0

        val uriModel = UriModel.parse(target.uri(extension.anchor.acquireUrl(target)))
        uriModel.trim(target)

        return Publication(
            uriModel,
            dependencyNotation,
            split[0],
            split[1],
            split[2],
            File(extension.output, target.name + ".json")
                .absolutePath.substring(target.rootDir.absolutePath.length + 1),
            target.name,
            target.projectDir.absolutePath.substring(target.rootDir.absolutePath.length + 1),
            category,
            DATE_FORMAT.format(timestamp),
            timestamp,
            sourceSet,
            sourceSummaries
        )
    }

    override fun endurance(target: Project, extension: DependencyPublish, entry: Publication) {
        val toJson = acquireGson().toJson(entry)
        val jsonFile = File(extension.output, "${target.name}.json")

        if (!jsonFile.parentFile.exists()) {
            jsonFile.mkdirs()
        }
        if (jsonFile.exists()) {
            jsonFile.createNewFile()
        }

        writeContentToFile(jsonFile, toJson)
    }

    private fun acquireGson(): Gson {
        return GsonBuilder()
            .serializeNulls()
            .create()
    }

    companion object {

        fun factory(target: Project): OnPublishAction {
            val pluginManager = target.pluginManager
            return when {
                pluginManager.hasPlugin(PluginId.Android.LIBRARY) -> {
                    AndroidPublishAction
                }
                pluginManager.hasPlugin(PluginId.Java.JAVA) -> {
                    JavaPublishAction
                }
                else -> {
                    throw NullPointerException("can not found sourceSet.")
                }
            }
        }

    }

}