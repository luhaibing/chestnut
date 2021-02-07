package com.mercer.magic.trigger

import com.mercer.magic.extension.DependencyReplace
import com.mercer.magic.model.DependencyEntry
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ProjectDependency

/**
 * @author          : mercer
 * @date            : 2021-02-07  01:29
 * @canonicalName   : com.mercer.magic.trigger.OnReplaceAction
 * @description     :
 */
interface OnReplaceAction {

    /**
     * 符号
     */
    val symbol: String

    /**
     * 拼接
     */
    fun splice(configuration: String, value: String): String

    /**
     * 拆解
     */
    fun unpick(value: String): String

    /**
     * 获取全部依赖
     */
    fun acquireDependencies(project: Project): List<Pair<String, List<Dependency>>>

    /**
     * 过滤需要处理的依赖
     */
    fun filterDependency(project: Project): (List<Pair<String, List<Dependency>>>) -> List<Pair<String, List<Dependency>>>

    /**
     * 转换类型
     */
    fun asType(): (List<Pair<String, List<Dependency>>>) -> List<Pair<String, List<ProjectDependency>>>

    /**
     * 展开依赖
     */
    fun unfoldTraverse(): (List<Pair<String, List<ProjectDependency>>>) -> List<Pair<String, ProjectDependency>>

    /**
     * 重新分组
     */
    fun regroup(): (List<Pair<String, ProjectDependency>>) -> List<Pair<ProjectDependency, List<String>>>

    fun filterExclude(extension: DependencyReplace):
                (List<Pair<ProjectDependency, List<String>>>) -> List<Pair<ProjectDependency, List<String>>>

    /**
     * 转换模型
     */
    fun conversion(project: Project): (List<Pair<ProjectDependency, List<String>>>) -> List<DependencyEntry>

    /**
     * 拦截器处理
     */
    fun interceptorHandle(extension: DependencyReplace):
                (List<DependencyEntry>) -> List<Pair<DependencyEntry, Dependency>>

    /**
     * 添加仓库
     */
    fun injectRepository(project: Project, extension: DependencyReplace):
                (List<Pair<DependencyEntry, Dependency>>) -> List<Pair<DependencyEntry, Dependency>>

    /**
     * 替换依赖
     */
    fun replace(project: Project): (List<Pair<DependencyEntry, Dependency>>) -> Unit

}