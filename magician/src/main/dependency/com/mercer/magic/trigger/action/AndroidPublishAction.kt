package com.mercer.magic.trigger.action

import com.android.build.gradle.LibraryExtension
import com.mercer.magic.constant.DependencyExt
import com.mercer.magic.enum.Category
import com.mercer.magic.model.SourceSet
import org.gradle.api.Project

/**
 * @author          : mercer
 * @date            : 2021-02-07  02:35
 * @canonicalName   : com.mercer.magic.trigger.action.AndroidPublishAction
 * @description     :
 */
object AndroidPublishAction : CommonOnPublishAction(Category.Android) {

    override fun source(target: Project): SourceSet {
        val container = (target.extensions.findByName(Category.Android.container)
                as LibraryExtension).sourceSets.findByName(DependencyExt.SOURCE_CHANNEL)
            ?: throw NullPointerException("can not found ${Category.Android.container}.${DependencyExt.SOURCE_CHANNEL}")
        return SourceSet(target.projectDir, container, target.buildFile)
    }

}