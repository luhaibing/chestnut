package com.mercer.magic.interceptor

import com.mercer.magic.trigger.OnDependencyStatusTrigger
import com.mercer.magic.model.DependencyEntry
import org.gradle.api.artifacts.Dependency
import com.mercer.magic.interceptor.response.Interceptor

/**
 * @author          : mercer
 * @date            : 2021-02-07  11:16
 * @canonicalName   : com.mercer.magic.interceptor.CheckStatusDependencyInterceptor
 * @desc            : 检查仓库依赖的状态
 */
class CheckStatusDependencyInterceptor(
    private val trigger: OnDependencyStatusTrigger,
) : DependencyInterceptor {

    override fun intercept(chain: Interceptor.Chain<DependencyEntry, Dependency>): Dependency {
        val input = chain.input()
        val status = input.status
        trigger.processDependency(status.second,input.dependency, status.first)
        return chain.proceed(input)
    }

}