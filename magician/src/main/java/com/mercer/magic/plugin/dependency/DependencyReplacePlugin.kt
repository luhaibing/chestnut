@file:Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "unused")

package com.mercer.magic.plugin.dependency

import com.mercer.magic.constant.Dependency
import com.mercer.magic.core.ProjectPlugin
import com.mercer.magic.core.interceptor.LoopChain
import com.mercer.magic.extension.dependency.DependencyReplace
import com.mercer.magic.extension.dependency.DependencyReplaceImpl
import com.mercer.magic.interceptor.DefaultDependencyInterceptor
import com.mercer.magic.model.dependency.DependencyEntry
import com.mercer.magic.model.enum.Status
import org.gradle.StartParameter
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.DependencySet
import org.gradle.api.artifacts.ProjectDependency

/**
 * @author      ：mercer
 * @date        ：2021-01-27  22:50
 * @description ：本地仓库依赖替换子模块依赖
 * com.mercer.magic.plugin.dependency.DependencyReplacePlugin
 */
class DependencyReplacePlugin : ProjectPlugin() {

    override fun process(project: Project, startParameter: StartParameter) {
        super.process(project, startParameter)
        // 先声明扩展属性接收
        declareExtension(project)
    }

    override fun afterEvaluateProcess(project: Project, startParameter: StartParameter) {
        super.afterEvaluateProcess(project, startParameter)

        // 获取扩展属性
        val extension: DependencyReplace = receiveExtension(project)

        handler(project, extension)

    }

    /**
     * 创建扩展
     */
    private fun declareExtension(project: Project) {
        project.extensions.create(
            DependencyReplace::class.java,
            Dependency.Extensions.REPLACE,
            DependencyReplaceImpl::class.java,
            project
        )
    }

    /**
     * 获取扩展
     */
    private fun receiveExtension(project: Project): DependencyReplace {
        return project.extensions.getByType(DependencyReplace::class.java)
    }

    /**
     * 处理
     */
    private fun handler(project: Project, extension: DependencyReplace) {

        // val pairs = configurationContainer

        val pairs: List<Pair<DependencyEntry, org.gradle.api.artifacts.Dependency?>> = project
            .configurations
            // 所有的依赖以及对应的依赖方式
            .asMap
            // 过滤空值
            .let(filterEmpty())
            // 展开
            .let(unfoldTraverseDependencies(project))
            // 重新分组
            .let(groupByDependency())
            // 变换值的结构
            .let(transformValues(project))
            .let(handleInterceptor(extension))
            .toList()

        val configurationContainer = project.configurations

        pairs.onEach { pair ->
            pair.first.configurations.onEach {
                configurationContainer.getByName(it).dependencies.remove(pair.first.dependency)
                if (pair.second != null) {
                    configurationContainer.getByName(it).dependencies.add(pair.second)
                }
            }
        }

    }

    private fun handleInterceptor(extension: DependencyReplace): (List<DependencyEntry>) -> List<Pair<DependencyEntry, org.gradle.api.artifacts.Dependency?>> {
        return {
            it.map { input ->
                val value = handleInterceptorByChain(input, extension)
                input to value
            }
        }
    }

    /**
     * 通过责任链获取新的依赖
     */
    private fun handleInterceptorByChain(
        input: DependencyEntry,
        extension: DependencyReplace
    ): org.gradle.api.artifacts.Dependency? {
        return LoopChain.start(input, DefaultDependencyInterceptor, extension.interceptors())
    }

    private fun transformValues(project: Project): (Map<ProjectDependency, List<String>>) -> List<DependencyEntry> {
        return { map ->
            map.map {
                DependencyEntry(project, it.key, it.value, Status.UNKNOWN, null)
            }
        }
    }

    /**
     * 重新分组
     */
    private fun groupByDependency(): (List<Pair<String, ProjectDependency>>) -> Map<ProjectDependency, List<String>> {
        return { pairs: List<Pair<String, ProjectDependency>> ->
            pairs.groupBy({ it.second }, { it.first.split("/").last() })
        }
    }


    /**
     * 过滤掉外部依赖
     * 只留下 ProjectDependency
     */
    private fun filterExternalDependencies(
        set: DependencySet, project: Project
    ): List<ProjectDependency> {
        return set
            .filterIsInstance<ProjectDependency>()
            .filter { it.name != project.name }
    }

    /**
     * 展开所有依赖
     */
    private fun unfoldTraverseDependencies(project: Project): (Map<String, Configuration>) -> List<Pair<String, ProjectDependency>> {
        return { map ->
            map.flatMap { entry ->
                filterExternalDependencies(entry.value.dependencies, project)
                    .map {
                        "${it.name}/${entry.key}" to it
                    }
            }
        }
    }

    private fun filterEmpty(): (Map<String, Configuration>) -> Map<String, Configuration> {
        return { it: Map<String, Configuration> ->
            it.filter {
                it.value.dependencies.isNotEmpty()
            }
        }
    }

    /**
     * 参数依赖路径是否存在和其下是否有文件
     */
    private fun checkExtensionParameter(extension: DependencyReplace) =
        !extension.srcDir.exists() || extension.srcDir.list() == null ||
                extension.srcDir.list().isEmpty()

}