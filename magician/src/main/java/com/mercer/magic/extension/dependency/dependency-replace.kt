package com.mercer.magic.extension.dependency

import com.mercer.magic.constant.Dependency
import com.mercer.magic.interceptor.CheckStatusDependencyInterceptor
import com.mercer.magic.interceptor.DependencyInterceptor
import com.mercer.magic.interceptor.TransformDependencyEntryInterceptor
import org.gradle.api.Project
import java.io.File

/**
 * @author      ：mercer
 * @date        ：2021-01-29  05:07
 * @description ：仓库依赖替换项目模块
 */
interface DependencyReplace {

    var srcDir: File

    fun srcDir(path: String)

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

}

open class DependencyReplaceImpl(
    private val project: Project
) : DependencyReplace {

    private val interceptors: MutableList<DependencyInterceptor> = arrayListOf()

    private val defaultInterceptors: MutableList<DependencyInterceptor> = arrayListOf()

    init {
        defaultInterceptors.add(TransformDependencyEntryInterceptor(this))
        defaultInterceptors.add(CheckStatusDependencyInterceptor())
        interceptors.addAll(defaultInterceptors)
    }

    /**
     * 哪些模块需要排除动态替换成二进制文件依赖
     */
    private val excludes: MutableList<String> = arrayListOf()

    override var srcDir: File = File(project.rootDir, Dependency.DEFAULT_DEPENDENCIES_PATH)

    override fun srcDir(path: String) {
        srcDir = File(project.projectDir, path)
    }

    override fun interceptors(): List<DependencyInterceptor> = interceptors

    override fun interceptor(interceptor: DependencyInterceptor) {
        interceptors.add(defaultInterceptors.size, interceptor)
    }

    override fun excludes(): MutableList<String> = excludes

    override fun exclude(vararg values: String) {
        TODO("Not yet implemented")
    }

    override fun exclude(values: Collection<String>) {
        values
            .map {
                if (it.startsWith(":")) {
                    it.substring(1)
                } else {
                    it
                }
            }
            .onEach {
                excludes.add(it)
            }
    }

}