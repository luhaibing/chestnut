package com.mercer.magic

import com.mercer.magic.service.Logger
import org.gradle.StartParameter
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.initialization.Settings
import javax.naming.OperationNotSupportedException

/**
 * @author          : mercer
 * @date            : 2021-02-05  23:03
 * @canonicalName   : com.mercer.magic.FoundationPlugin
 * @description     : 插件基本操作
 */
abstract class FoundationPlugin<T, E> : Plugin<T> {

    open val logger: Logger = DefaultLogger()

    override fun apply(target: T) {
        when (target) {
            is Project -> {
                declareExtension(target)
                if (!proceed(target, target.gradle.startParameter)) {
                    return
                }
                handle(target, target.gradle.startParameter)
                target.afterEvaluate {
                    val extension: E? = receiveExtension(target)
                    if (!afterEvaluateProceed(target, extension, target.gradle.startParameter)) {
                        return@afterEvaluate
                    }
                    afterEvaluateHandle(target, extension, target.gradle.startParameter)
                }
            }
            is Settings -> {
                declareExtension(target)
                if (!proceed(target, target.gradle.startParameter)) {
                    return
                }
                handle(target, target.gradle.startParameter)
                target.gradle.settingsEvaluated {
                    val extension: E? = receiveExtension(target)
                    if (!afterEvaluateProceed(target, extension, target.gradle.startParameter)) {
                        return@settingsEvaluated
                    }
                    afterEvaluateHandle(target, extension, target.gradle.startParameter)
                }
            }
            else -> run {
                throw OperationNotSupportedException("can not handler ${target!!::class.java.canonicalName}.")
            }
        }
    }

    /**
     * 声明扩展
     */
    open fun declareExtension(target: T) {
    }

    /**
     * 接收扩展
     */
    open fun receiveExtension(target: T): E? {
        return null
    }

    /**
     * 初始化期,判定是否继续
     */
    open fun proceed(target: T, startParameter: StartParameter): Boolean {
        return true
    }

    /**
     * 初始化时期处理
     */
    open fun handle(target: T, startParameter: StartParameter) {
    }

    /**
     * 配置期完成后,判定是否继续
     */
    open fun afterEvaluateProceed(target: T, extension: E?, startParameter: StartParameter): Boolean {
        return true
    }

    /**
     * 配置期完成后处理
     */
    open fun afterEvaluateHandle(target: T, extension: E?, startParameter: StartParameter) {
    }

}