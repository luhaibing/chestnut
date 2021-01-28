package com.mercer.magic.interceptor

import com.mercer.magic.core.interceptor.Interceptor
import com.mercer.magic.model.dependency.DependencyEntry
import com.mercer.magic.model.enum.Status
import org.gradle.api.artifacts.Dependency

object DefaultDependencyInterceptor : DependencyInterceptor {

    override fun intercept(chain: Interceptor.Chain<DependencyEntry, Dependency>): Dependency? {
        val input = chain.input()
        return if (input.status == Status.VALID && input.entry != null) {
            input.target.dependencies.create(input.entry!!.dependencyNotation)
        } else {
            input.dependency
        }
    }

}