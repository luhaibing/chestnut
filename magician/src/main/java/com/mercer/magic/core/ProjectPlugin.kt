package com.mercer.magic.core

import org.gradle.StartParameter
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * @author      ：mercer
 * @date        ：2021-01-28  03:18
 * @description ：项目型插件
 */
abstract class ProjectPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        val startParameter = target.gradle.startParameter
        process(target, startParameter)
        target.afterEvaluate {
            afterEvaluateProcess(it, startParameter)
        }
    }

    open fun process(project: Project, startParameter: StartParameter) {

    }

    open fun afterEvaluateProcess(project: Project, startParameter: StartParameter) {

    }

}