package com.mercer.magic.model

import com.mercer.magic.enum.Status
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import java.io.File

/**
 * @author          : mercer
 * @date            : 2021-02-07  04:43
 * @canonicalName   : com.mercer.magic.model.DependencyEntry
 * @description     : 拦截器处理时的条目
 */
interface DependencyEntry {

    // 正在处理的模块
    val target: Project

    // 原始依赖
    val dependency: Dependency

    // 依赖方式
    val configurations: List<String>

    // 仓库依赖的状态
    val status: Pair<File, Status>

    val entry: Publication?

}