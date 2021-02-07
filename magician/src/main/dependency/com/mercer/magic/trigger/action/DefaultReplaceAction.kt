package com.mercer.magic.trigger.action

import com.mercer.magic.trigger.OnReplaceAction
import com.mercer.magic.extension.DependencyReplace
import com.mercer.magic.interceptor.LoopChain
import com.mercer.magic.model.DefaultDependencyEntry
import com.mercer.magic.model.DependencyEntry
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.Dependency
import org.gradle.api.artifacts.ProjectDependency

/**
 * @author          : mercer
 * @date            : 2021-02-07  02:36
 * @canonicalName   : com.mercer.magic.trigger.action.impl.OnReplaceActionImpl
 * @description     :
 */
class DefaultReplaceAction : OnReplaceAction {

    override val symbol: String = ":"

    override fun splice(configuration: String, value: String): String {
        return "$configuration:$value"
    }

    override fun unpick(value: String): String {
        return value.split(symbol).last()
    }

    override fun acquireDependencies(project: Project): List<Pair<String, List<Dependency>>> {
        return project.configurations.asMap.map { entry: Map.Entry<String, Configuration> ->
            entry.key to entry.value.dependencies.toList()
        }
    }

    override fun filterDependency(project: Project): (List<Pair<String, List<Dependency>>>) -> List<Pair<String, List<Dependency>>> {
        return { pairs: List<Pair<String, List<Dependency>>> ->
            pairs
                .filter { it.second.isNotEmpty() }
                .map { pair ->
                    pair.first to pair.second.filter {
                        it.name != project.name
                    }
                }
        }
    }

    override fun asType(): (List<Pair<String, List<Dependency>>>) -> List<Pair<String, List<ProjectDependency>>> {
        return { pairs ->
            pairs.map { pair ->
                pair.first to pair.second
                    .filterIsInstance<ProjectDependency>()
            }
        }
    }

    override fun unfoldTraverse(): (List<Pair<String, List<ProjectDependency>>>) -> List<Pair<String, ProjectDependency>> {
        return { pairs ->
            pairs.flatMap { pair ->
                pair.second
                    .map {
                        splice(it.name, pair.first) to it
                    }
            }
        }
    }

    override fun regroup(): (List<Pair<String, ProjectDependency>>) -> List<Pair<ProjectDependency, List<String>>> {
        return { pairs: List<Pair<String, ProjectDependency>> ->
            pairs
                .groupBy({
                    it.second
                }, {
                    unpick(it.first)
                })
                .map {
                    it.key to it.value
                }
        }
    }

    override fun filterExclude(extension: DependencyReplace): (List<Pair<ProjectDependency, List<String>>>) -> List<Pair<ProjectDependency, List<String>>> {
        val excludes: List<String> = extension.excludes()
        return { pairs ->
            pairs.filter {
                !excludes.contains(it.first.name)
            }
        }
    }

    override fun conversion(project: Project): (List<Pair<ProjectDependency, List<String>>>) -> List<DependencyEntry> {
        return { pairs ->
            pairs.map {
                DefaultDependencyEntry(project, it.first, it.second)
            }
        }
    }

    override fun interceptorHandle(extension: DependencyReplace): (List<DependencyEntry>) -> List<Pair<DependencyEntry, Dependency>> {
        return { pairs ->
            pairs.map {
                val value = LoopChain.start(it, extension.interceptors())
                it to value
            }
        }
    }

    override fun injectRepository(
        project: Project, extension: DependencyReplace
    ): (List<Pair<DependencyEntry, Dependency>>) -> List<Pair<DependencyEntry, Dependency>> {
        return { pairs ->
            extension.repositoryTrigger.inject()
            pairs
        }
    }

    override fun replace(project: Project): (List<Pair<DependencyEntry, Dependency>>) -> Unit {
        val configurationContainer = project.configurations
        return { pairs ->
            pairs.onEach { pair ->
                val originDependency = pair.first.dependency
                val newDependency = pair.second
                if (originDependency != newDependency) {
                    pair.first.configurations.onEach {
                        val configuration = configurationContainer.getByName(it)
                        configuration.dependencies.remove(originDependency)
                        configuration.dependencies.add(newDependency)
                    }
                }
            }
        }
    }

}