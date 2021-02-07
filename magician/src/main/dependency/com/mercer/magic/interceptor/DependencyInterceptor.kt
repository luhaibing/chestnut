package com.mercer.magic.interceptor

import com.mercer.magic.model.DependencyEntry
import org.gradle.api.artifacts.Dependency

/**
 * @author          : mercer
 * @date            : 2021-02-07  04:46
 * @canonicalName   : com.mercer.magic.interceptor.DependencyInterceptor
 * @description     : 依赖处理拦截器
 */
interface DependencyInterceptor : Interceptor<DependencyEntry, Dependency>