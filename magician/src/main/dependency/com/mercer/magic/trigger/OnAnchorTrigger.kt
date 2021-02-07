package com.mercer.magic.trigger

import org.gradle.api.Project
import org.gradle.api.Task
import java.net.URI

/**
 * @author          : mercer
 * @date            : 2021-02-06  19:48
 * @canonicalName   : com.mercer.magic.trigger.OnAnchorTrigger2
 * @description     : 任务锚点
 */
interface OnAnchorTrigger {

    /**
     * 任务名
     */
    var name: String

    /**
     * 获取任务锚点
     */
    fun acquireAnchor(target: Project): Task

    /**
     * 依赖记法
     */
    fun notation(target: Project): String

    fun acquireUrl(target: Project): String

}