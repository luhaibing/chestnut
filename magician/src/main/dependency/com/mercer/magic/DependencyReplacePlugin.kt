package com.mercer.magic

import com.mercer.magic.trigger.OnDependencyCallBack
import com.mercer.magic.trigger.OnReplaceAction
import com.mercer.magic.trigger.action.DefaultReplaceAction
import com.mercer.magic.constant.DependencyExt
import com.mercer.magic.extension.DefaultDependencyReplace
import com.mercer.magic.extension.DependencyReplace
import org.gradle.StartParameter
import org.gradle.api.Project

/**
 * @author          : mercer
 * @date            : 2021-02-06  00:09
 * @canonicalName   : com.mercer.magic.DependencyReplacePlugin
 * @description     : 本地模块动态替换为仓库依赖
 */
class DependencyReplacePlugin : FoundationPlugin<Project, DependencyReplace>(),
    OnDependencyCallBack<OnReplaceAction, DependencyReplace> {

    override fun declareExtension(target: Project) {
        super.declareExtension(target)
        target.extensions.create(
            DependencyReplace::class.java, DependencyExt.REPLACE_KEY,
            DefaultDependencyReplace::class.java, target, logger
        )
    }

    override fun receiveExtension(target: Project): DependencyReplace {
        val extension =
            (target.extensions.findByName(DependencyExt.REPLACE_KEY) as? DefaultDependencyReplace
                ?: throw NullPointerException("can not receive extensions findByName(${DependencyExt.REPLACE_KEY})."))
        extension.prepare()
        return extension
    }

    override fun afterEvaluateHandle(
        target: Project,
        extension: DependencyReplace?,
        startParameter: StartParameter
    ) {
        super.afterEvaluateHandle(target, extension, startParameter)
        extension ?: throw NullPointerException("extension can not be null.")
        action = DefaultReplaceAction()
        process(target, extension)
    }

    override lateinit var action: OnReplaceAction

    override fun process(target: Project, extension: DependencyReplace) {
        action
            .acquireDependencies(target)
            .let(action.filterDependency(target))
            .let(action.asType())
            .let(action.unfoldTraverse())
            .let(action.regroup())
            .let(action.conversion(target))
            .let(action.interceptorHandle(extension))
            .let(action.injectRepository(target, extension))
            .let(action.replace(target))
    }

}