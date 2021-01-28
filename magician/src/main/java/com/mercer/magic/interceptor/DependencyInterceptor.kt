package com.mercer.magic.interceptor

import com.mercer.magic.core.interceptor.Interceptor
import org.gradle.api.artifacts.Dependency
import com.mercer.magic.model.dependency.DependencyEntry

/**
 * @author      ：mercer
 * @date        ：2021-01-28  22:58
 * @description ：依赖处理拦截器
 */
interface DependencyInterceptor : Interceptor<DependencyEntry, Dependency>