package com.mercer.magic.trigger

import com.mercer.magic.enum.Category
import com.mercer.magic.extension.DependencyPublish
import com.mercer.magic.model.Publication
import com.mercer.magic.model.SourceSet
import com.mercer.magic.model.SourceSummary
import org.gradle.api.Project

/**
 * @author          : mercer
 * @date            : 2021-02-07  01:27
 * @canonicalName   : com.mercer.magic.trigger.OnPublishAction
 * @description     :
 */
interface OnPublishAction {

    val category: Category

    /**
     * 获取编译目录
     */
    fun source(target: Project): SourceSet

    /**
     * 获取信息摘要
     */
    fun sourceSummaries(target: Project, sourceSet: SourceSet): List<SourceSummary>

    /**
     * 拼接需要本地持久化的发布信息的 json
     */
    fun splicePublication(
        target: Project, extension: DependencyPublish,
        sourceSet: SourceSet, sourceSummaries: List<SourceSummary>
    ): Publication

    /**
     * 本地持久化
     */
    fun endurance(target: Project, extension: DependencyPublish, entry: Publication)

}