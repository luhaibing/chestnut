package com.mercer.magic.trigger

import org.gradle.api.Project

/**
 * @author          : mercer
 * @date            : 2021-02-07  01:30
 * @canonicalName   : com.mercer.magic.trigger.OnDependencyCallBack
 * @description     :
 */
interface OnDependencyCallBack<Action,Extension> {

    val action: Action

    fun process(target: Project, extension: Extension)

}