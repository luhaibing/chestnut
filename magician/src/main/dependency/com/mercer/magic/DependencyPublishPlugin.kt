package com.mercer.magic

import com.mercer.magic.trigger.action.CommonOnPublishAction
import com.mercer.magic.trigger.OnDependencyCallBack
import com.mercer.magic.trigger.OnPublishAction
import com.mercer.magic.constant.DependencyExt
import com.mercer.magic.extension.DefaultDependencyPublish
import com.mercer.magic.extension.DependencyPublish
import org.gradle.StartParameter
import org.gradle.api.Project

/**
 * @author          : mercer
 * @date            : 2021-02-06  00:08
 * @canonicalName   : com.mercer.magic.DependencyPublishPlugin
 * @description     : 模块发布依赖到本地仓库
 */
class DependencyPublishPlugin : FoundationPlugin<Project, DependencyPublish>(),
    OnDependencyCallBack<OnPublishAction, DependencyPublish> {

    override lateinit var action: OnPublishAction

    override fun declareExtension(target: Project) {
        super.declareExtension(target)
        target.extensions.create(
            DependencyPublish::class.java, DependencyExt.PUBLISH_KEY,
            DefaultDependencyPublish::class.java, target
        )
    }

    override fun afterEvaluateProceed(
        target: Project,
        extension: DependencyPublish?,
        startParameter: StartParameter
    ): Boolean {
        if (extension?.anchor == null) {
            return false
        }
        val anchor = extension.anchor.name
        val module = target.name
        return startParameter.taskNames.any {
            it == anchor || it == "${module}:${anchor}" || it == ":${module}:${anchor}"
        }
    }

    override fun receiveExtension(target: Project): DependencyPublish {
        val extension =
            (target.extensions.findByName(DependencyExt.PUBLISH_KEY) as? DefaultDependencyPublish
                ?: throw NullPointerException("can not receive extensions findByName(${DependencyExt.PUBLISH_KEY})."))
        extension.prepare()
        return extension
    }

    override fun afterEvaluateHandle(
        target: Project, extension: DependencyPublish?, startParameter: StartParameter
    ) {
        super.afterEvaluateHandle(target, extension, startParameter)
        extension ?: throw NullPointerException("extension can not be null.")
        // TODO: 2/7/21
        println("notation : ${extension.anchor.notation(target)}")
        extension.anchor.acquireAnchor(target).doLast {
            action = CommonOnPublishAction.factory(target)
            process(target, extension)
        }
    }

    override fun process(target: Project, extension: DependencyPublish) {
        val sourceSet = action.source(target)
        val sourceSummaries = action.sourceSummaries(target, sourceSet)
        val publication = action.splicePublication(target, extension, sourceSet, sourceSummaries)
        action.endurance(target, extension, publication)
    }

}