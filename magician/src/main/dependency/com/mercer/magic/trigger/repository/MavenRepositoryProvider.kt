package com.mercer.magic.trigger.repository

import com.mercer.magic.constant.DependencyExt
import com.mercer.magic.enum.RepositoryPlugin
import com.mercer.magic.extension.DependencyPublish
import com.mercer.magic.trigger.OnRepositoryProvider
import com.mercer.magic.trigger.anchor.MavenAnchorTrigger
import com.mercer.magic.util.withGroovyBuilder
import org.gradle.api.Project
import org.gradle.api.internal.file.BaseDirFileResolver
import java.net.URI

/**
 * @author          : mercer
 * @date            : 2021-02-06  20:13
 * @canonicalName   : com.mercer.magic.trigger.repository.MavenRepositoryProvider
 * @description     :
 */
@Suppress("unused", "SpellCheckingInspection")
class MavenRepositoryProvider(private val target: Project) :
    MavenAnchorTrigger(), OnRepositoryProvider {

    override var url: URI? = null

    override fun url(value: String): URI {
        url = BaseDirFileResolver(target.rootDir).resolveUri(value) // target.rootProject.uri(value)
        return url ?: throw NullPointerException("url can not be null.")
    }

    override var groupId: String = ""

    override var artifactId: String = target.name

    override var version: String = ""

    override fun prepare(extension: DependencyPublish) {
        target.pluginManager.apply(RepositoryPlugin.MAVEN.id)
        target.afterEvaluate {

            if (url == null) {
                url = target.uri(extension.output)
            }

            if (groupId.trim().isEmpty()) {
                groupId = if (target.group.toString().trim().isNotEmpty() &&
                    target.group.toString().trim() != target.rootProject.name
                ) {
                    // 指定了 project.group
                    target.group.toString().trim()
                } else if (target.rootProject.group.toString().trim().isNotEmpty()) {
                    // 指定了 rootProject.group
                    target.rootProject.group.toString().trim()
                } else {
                    // 默认
                    target.group.toString().trim()
                }
            }

            if (version.trim().isEmpty()) {
                version = if (target.version.toString().trim().isNotEmpty() &&
                    target.version.toString().trim() != DependencyExt.UNSPECIFIED
                ) {
                    target.version.toString()
                } else {
                    DependencyExt.DEFAULT_VERSION
                }
            }

            if (artifactId.trim().isEmpty()) {
                artifactId = target.name
            }

        }
    }

    override fun apply() {
        acquireAnchor(target).apply {
            withGroovyBuilder {
                "repositories"{
                    "mavenDeployer"{
                        "repository"("url" to target.uri(url!!))
                        "pom"{
                            "project"{
                                "groupId"(groupId)
                                "artifactId"(artifactId)
                                "version"(version)
                            }
                        }
                    }
                }
            }
        }
    }

}