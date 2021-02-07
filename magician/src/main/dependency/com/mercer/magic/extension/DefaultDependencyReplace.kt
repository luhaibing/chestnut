package com.mercer.magic.extension

import com.mercer.magic.trigger.OnDependencyStatusTrigger
import com.mercer.magic.trigger.OnRepositoryTrigger
import com.mercer.magic.trigger.impl.DefaultComparableListener
import com.mercer.magic.trigger.impl.DefaultDependencyStatusTrigger
import com.mercer.magic.trigger.repository.DefaultRepositoryTrigger
import com.mercer.magic.constant.DependencyExt
import com.mercer.magic.interceptor.*
import com.mercer.magic.service.Logger
import org.gradle.api.Project
import java.io.File

/**
 * @author          : mercer
 * @date            : 2021-02-06  00:16
 * @canonicalName   : com.mercer.magic.model.DefaultDependencyReplace
 * @description     :
 */
open class DefaultDependencyReplace(
    private val project: Project, logger: Logger
) : DependencyReplace {


    /**
     * 哪些模块需要排除动态替换成二进制文件依赖
     */
    private val excludes: MutableList<String> = arrayListOf()

    override var input: File = File(project.rootDir, DependencyExt.DEFAULT_FOLDER)

    private val interceptors: MutableList<DependencyInterceptor> = arrayListOf()

    private val headerInterceptors: MutableList<DependencyInterceptor> = arrayListOf()
    private val footerInterceptors: MutableList<DependencyInterceptor> = arrayListOf()

    override val statusTrigger: OnDependencyStatusTrigger =
        DefaultDependencyStatusTrigger(logger)

    override val repositoryTrigger: OnRepositoryTrigger = DefaultRepositoryTrigger(project)

    override fun input(value: String) {
        input = File(project.rootDir, value)
    }

    override fun interceptors(): List<DependencyInterceptor> = interceptors
    override fun interceptor(interceptor: DependencyInterceptor) {
        interceptors.add(headerInterceptors.size, interceptor)
    }

    override fun excludes(): MutableList<String> = excludes
    override fun exclude(vararg values: String) {
        exclude(values.toList())
    }

    override fun exclude(values: Collection<String>) {
        values
            .map {
                trim(it)
            }
            .onEach {
                excludes.add(it)
            }
    }

    fun prepare() {
        if (interceptors.isEmpty()) {
            return
        }
        if (headerInterceptors.isEmpty()) {
            initialHeaderInterceptor()
            val tmp = arrayListOf<DependencyInterceptor>()
            tmp.addAll(interceptors)
            interceptors.clear()
            interceptors.addAll(headerInterceptors)
            interceptors.addAll(tmp)
        }
        if (footerInterceptors.isEmpty()) {
            initialFooterInterceptor()
            interceptors.addAll(footerInterceptors)
        }
    }

    private fun initialHeaderInterceptor() {
        headerInterceptors.add(AnalyseJsonFileInterceptor(this))
        headerInterceptors.add(CheckValidInterceptor(DefaultComparableListener()))
    }

    private fun initialFooterInterceptor() {
        footerInterceptors.add(CheckStatusDependencyInterceptor(statusTrigger))
        footerInterceptors.add(DefaultDependencyInterceptor(repositoryTrigger))
    }

    private fun trim(value: String): String {
        return if (value.startsWith(":")) {
            value.substring(1)
        } else {
            value
        }
    }

}