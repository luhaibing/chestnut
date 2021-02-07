package com.mercer.magic.trigger.anchor

import com.mercer.magic.enum.RepositoryPlugin
import com.mercer.magic.model.RepositoryModel
import com.mercer.magic.trigger.OnAnchorTrigger
import com.mercer.magic.util.asRepository
import org.gradle.api.Project
import org.gradle.api.publication.maven.internal.deployer.BaseMavenDeployer
import org.gradle.api.publication.maven.internal.deployer.DefaultGroovyMavenDeployer
import org.gradle.api.publication.maven.internal.deployer.MavenRemoteRepository
import org.gradle.api.tasks.Upload

/**
 * @author          : mercer
 * @date            : 2021-02-06  19:59
 * @canonicalName   : com.mercer.magic.trigger.anchor.MavenAnchorTrigger
 * @description     :
 */
@Suppress("SpellCheckingInspection")
open class MavenAnchorTrigger : OnAnchorTrigger {

    override var name: String = RepositoryPlugin.MAVEN.anchor

    override fun acquireAnchor(target: Project): Upload {
        return target.tasks.findByName(name) as? Upload
            ?: throw NullPointerException("can not get task by findByName(${name})")
    }

    override fun notation(target: Project): String {
        val model: RepositoryModel = mavenDeployer(target).pom.model.asRepository()
        return "${model.groupId}:${model.artifactId}:${model.version}"
    }

    override fun acquireUrl(target: Project): String {
        val artifactRepository = mavenDeployer(target)
        val remoteRepositoryField =
            BaseMavenDeployer::class.java.getDeclaredField("remoteRepository")
        remoteRepositoryField.isAccessible = true
        return (remoteRepositoryField.get(artifactRepository) as MavenRemoteRepository).url
    }

    private fun mavenDeployer(target: Project) =
        (acquireAnchor(target).repositories.first() as DefaultGroovyMavenDeployer)

}