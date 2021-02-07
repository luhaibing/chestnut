package com.mercer.magic.extension

import com.mercer.magic.trigger.OnAnchorTrigger
import groovy.lang.Closure
import org.gradle.api.Action
import java.io.Closeable
import java.io.File

/**
 * @author          : mercer
 * @date            : 2021-02-06  19:44
 * @canonicalName   : com.mercer.magic.extension.DependencyPublish2
 * @description     : 依赖发布插件的扩展属性
 */
interface DependencyPublish {

    /**
     * 存放根据代码和资源摘要的生成的json 数据
     */
    val output: File

    fun output(value: String): File

    val anchor: OnAnchorTrigger

    fun anchor(value: OnAnchorTrigger)

    // TODO: 2/6/21
    // fun anchor(value: Closure<OnAnchorTrigger>)
    // fun anchor(value: Action<OnAnchorTrigger>)
    // fun <T : OnAnchorTrigger> anchor(value: Action<T>)

}