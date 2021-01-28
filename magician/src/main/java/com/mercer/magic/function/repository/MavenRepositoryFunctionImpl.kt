package com.mercer.magic.function.repository

import com.mercer.magic.core.function.OnRepositoryFunction
import com.mercer.magic.extension.dependency.DependencyPublish
import com.mercer.magic.function.anchor.MavenAnchorFunctionImpl
import com.mercer.magic.util.withGroovyBuilder
import org.gradle.api.Project
import org.gradle.api.tasks.Upload

/**
 * @author      ：mercer
 * @date        ：2021-01-29  01:43
 * @description ：maven 仓库实现
 */
class MavenRepositoryFunctionImpl : MavenAnchorFunctionImpl(), OnRepositoryFunction {

    override fun apply(target: Project, extension: DependencyPublish) {
        target.pluginManager.apply(pluginId)
        acquire(target).apply {
            withGroovyBuilder {
                withGroovyBuilder {
                    "repositories"{
                        "mavenDeployer"{
                            "repository"("url" to project.uri(extension.output))
                            "pom"{
                                "project"{
                                    "groupId"(extension.groupId)
                                    "artifactId"(extension.artifactId)
                                    "version"(extension.version)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}