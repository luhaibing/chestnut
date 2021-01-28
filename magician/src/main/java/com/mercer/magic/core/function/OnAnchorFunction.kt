package com.mercer.magic.core.function

import org.gradle.api.Project
import org.gradle.api.Task
import java.net.URI

/**
 * @author      ：mercer
 * @date        ：2021-01-28  22:34
 * @description ：任务锚点回调
 */
interface OnAnchorFunction {

    /**
     * 插件ID
     */
    val pluginId: String

    /**
     * 锚点名
     */
    val anchor: String

    /**
     * 获取任务锚点
     */
    fun acquire(target: Project): Task

    /**
     * 仓库路径描述
     */
    fun url(target: Project): URI

    /**
     * 依赖记法
     */
    fun notation(target: Project): String

}