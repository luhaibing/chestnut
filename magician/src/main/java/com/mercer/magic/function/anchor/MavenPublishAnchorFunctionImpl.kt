package com.mercer.magic.function.anchor

import com.mercer.magic.core.function.OnAnchorFunction
import com.mercer.magic.model.enum.Repository
import org.gradle.api.Project
import org.gradle.api.publish.maven.tasks.PublishToMavenRepository
import java.net.URI

/**
 * @author      ：mercer
 * @date        ：2021-01-29  04:03
 * @description ：maven-publish 仓库锚点
 */
class MavenPublishAnchorFunctionImpl : OnAnchorFunction {

    override val pluginId: String = Repository.MAVEN_PUBLISH.id

    override val anchor: String = Repository.MAVEN_PUBLISH.anchor

    override fun acquire(target: Project): PublishToMavenRepository {
        return target.tasks.findByName(Repository.MAVEN_PUBLISH.anchor) as? PublishToMavenRepository
            ?: throw NullPointerException("can not get task by findByName(${anchor})")
    }

    override fun url(target: Project): URI {
       return acquire(target).repository.url
    }

    override fun notation(target: Project): String {
        val repository =
            (target.tasks.findByName(Repository.MAVEN_PUBLISH.anchor) as PublishToMavenRepository).publication
        return "${repository.groupId}:${repository.artifactId}:${repository.version}"
    }

}