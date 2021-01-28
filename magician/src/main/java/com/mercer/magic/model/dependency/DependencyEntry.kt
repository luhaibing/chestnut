package com.mercer.magic.model.dependency

import com.mercer.magic.model.enum.Status
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency

/**
 * @author      ：mercer
 * @date        ：2021-01-28  22:59
 * @description ：拦截器处理时的条目
 */
class DependencyEntry(
    // 正在处理的模块
    val target: Project,
    // 原始依赖
    val dependency: Dependency,
    // 依赖方式
    val configurations: List<String>,
    // 本地仓库依赖的状态
    var status: Status,
    // 本地仓库的依赖
    var entry: Publication?
)