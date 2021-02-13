package com.mercer.magic.trigger.action

import com.mercer.magic.constant.DependencyExt
import com.mercer.magic.enum.Category
import com.mercer.magic.model.SourceSet
import org.gradle.api.Project
import org.gradle.api.internal.tasks.DefaultSourceSetContainer

/**
 * @author          : mercer
 * @date            : 2021-02-07  02:35
 * @canonicalName   : com.mercer.magic.trigger.action.JavaPublishAction
 * @description     :
 */
object JavaPublishAction : CommonOnPublishAction(Category.Java) {

    override fun source(target: Project): SourceSet {

        val container = (target.extensions.findByName(Category.Java.container)
                as DefaultSourceSetContainer).findByName(DependencyExt.SOURCE_CHANNEL)
            ?: throw NullPointerException("can not found ${Category.Java.container}.${DependencyExt.SOURCE_CHANNEL}")
        return SourceSet.java(target, container)

    }

}