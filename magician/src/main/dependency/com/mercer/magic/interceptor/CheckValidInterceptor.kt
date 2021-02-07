package com.mercer.magic.interceptor

import com.mercer.magic.trigger.OnComparableListener
import com.mercer.magic.enum.Status
import com.mercer.magic.model.DefaultDependencyEntry
import com.mercer.magic.model.DependencyEntry
import com.mercer.magic.model.SourceSummary
import org.gradle.api.artifacts.Dependency
import java.io.File

/**
 * @author          : mercer
 * @date            : 2021-02-07  12:18
 * @canonicalName   : com.mercer.magic.interceptor.CheckValidInterceptor
 * @desc            : 检查 仓库中的依赖是否有效
 */
// 对比器
class CheckValidInterceptor(
    private val comparableListener: OnComparableListener<List<SourceSummary>, List<File>>
) : DependencyInterceptor {

    override fun intercept(chain: Interceptor.Chain<DependencyEntry, Dependency>): Dependency {
        val input: DefaultDependencyEntry = chain.input() as DefaultDependencyEntry
        val status = input.status
        if (!status.second.isUndecided()) {
            return chain.proceed(input)
        }
        val publication = input.entry ?: throw NullPointerException("publication can not be null.")

        val projectDir = input.target.rootProject.project(input.dependency.name).projectDir.absolutePath
        val dst = publication.resourceSummaries
        val src = publication.sourceSets.collects(projectDir)

        if (comparableListener.compare(dst, src)) {
            input.status = Pair(status.first, Status.VALID)
        } else {
            input.status = Pair(status.first, Status.EXPIRED)
        }
        return chain.proceed(input)
    }

}