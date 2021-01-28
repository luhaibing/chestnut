package com.mercer.magic.function.anchor

import com.mercer.magic.core.function.OnAnchorFunction
import com.mercer.magic.model.RepositoryModel
import com.mercer.magic.model.enum.Repository
import com.mercer.magic.util.asRepository
import org.gradle.api.Project
import org.gradle.api.publication.maven.internal.deployer.BaseMavenDeployer
import org.gradle.api.publication.maven.internal.deployer.DefaultGroovyMavenDeployer
import org.gradle.api.publish.internal.DefaultPublishingExtension
import org.gradle.api.tasks.Upload
import java.net.URI
import org.gradle.api.publication.maven.internal.deployer.MavenRemoteRepository

/**
 * @author      ：mercer
 * @date        ：2021-01-29  01:36
 * @description ：maven 仓库锚点
 */
open class MavenAnchorFunctionImpl : OnAnchorFunction {

    override val pluginId: String = Repository.MAVEN.id

    override val anchor: String = Repository.MAVEN.anchor

    override fun acquire(target: Project): Upload {
        return target.tasks.findByName(anchor) as? Upload
            ?: throw NullPointerException("can not get task by findByName(${anchor})")
    }

    override fun url(target: Project): URI {
        val mavenDeployer = mavenDeployer(target)
        val remoteRepositoryField =
            BaseMavenDeployer::class.java.getDeclaredField("remoteRepository")
        remoteRepositoryField.isAccessible = true
        val mavenRemoteRepository =
            remoteRepositoryField.get(mavenDeployer) as MavenRemoteRepository
        return URI(mavenRemoteRepository.url)
    }

    override fun notation(target: Project): String {
        val model: RepositoryModel = mavenDeployer(target).pom.model.asRepository()
        return "${model.groupId}:${model.artifactId}:${model.version}"
    }

    private fun mavenDeployer(target: Project) =
        (acquire(target).repositories.first() as DefaultGroovyMavenDeployer)

}