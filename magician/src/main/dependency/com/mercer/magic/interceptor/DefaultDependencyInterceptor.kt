package com.mercer.magic.interceptor

import com.mercer.magic.trigger.OnRepositoryTrigger
import com.mercer.magic.enum.Status
import com.mercer.magic.model.DependencyEntry
import com.mercer.magic.model.UriModel
import org.gradle.api.artifacts.Dependency
import java.io.File
import com.mercer.magic.interceptor.response.Interceptor


/**
 * @author          : mercer
 * @date            : 2021-02-07  11:25
 * @canonicalName   : com.mercer.magic.interceptor.DefaultDependencyInterceptor
 * @desc            : 拦截器末端操作
 */
class DefaultDependencyInterceptor(private val repositoryListener: OnRepositoryTrigger) :
    DependencyInterceptor {

    override fun intercept(chain: Interceptor.Chain<DependencyEntry, Dependency>): Dependency {
        val input: DependencyEntry = chain.input()
        val status: Pair<File, Status> = input.status
        // 条目
        val entry = input.entry
        return if (status.second.isValid() && entry != null) {
            val uriModel: UriModel = entry.uri
            repositoryListener.request(uriModel.toURI(input.target.rootProject))
            input.target.dependencies.create(entry.dependencyNotation)
        } else {
            input.dependency
        }
    }

}