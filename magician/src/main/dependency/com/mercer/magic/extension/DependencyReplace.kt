package com.mercer.magic.extension

import com.mercer.magic.trigger.OnDependencyStatusTrigger
import com.mercer.magic.trigger.OnRepositoryTrigger
import com.mercer.magic.interceptor.DependencyInterceptor
import java.io.File

/**
 * @author          : mercer
 * @date            : 2021-02-06  00:11
 * @canonicalName   : com.mercer.magic.model.DependencyReplace
 * @description     : 依赖替换插件的扩展属性
 */
interface DependencyReplace {

    val input: File

    fun input(value: String)

    /**
     * 依赖处理的拦截器
     */
    fun interceptors(): List<DependencyInterceptor>

    fun interceptor(interceptor: DependencyInterceptor)

    /**
     * 不要替换的本地模块
     */
    fun excludes(): MutableList<String>

    fun exclude(vararg values: String)

    fun exclude(values: Collection<String>)

    val statusTrigger: OnDependencyStatusTrigger

    val repositoryTrigger: OnRepositoryTrigger

}