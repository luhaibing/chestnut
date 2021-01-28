package com.mercer.magic.interceptor

import com.mercer.magic.core.interceptor.Interceptor
import com.mercer.magic.model.dependency.DependencyEntry
import com.mercer.magic.model.dependency.Publication
import com.mercer.magic.model.enum.Status
import org.apache.commons.io.FileUtils
import org.gradle.api.artifacts.Dependency
import java.io.File

/**
 * 检查 数据实体 的状态
 * 如果 状态 是已经过期或者 是遗失,就将对应的.json 文件和 依赖文件夹删除
 */
class CheckStatusDependencyInterceptor : DependencyInterceptor {

    override fun intercept(chain: Interceptor.Chain<DependencyEntry, Dependency>): Dependency? {

        val input = chain.input()

        if (input.status == Status.LOST) {
            return chain.proceed(input)
        }

        val project = input.target
        val entry: Publication = input.entry!!

        val collects =
            entry.sourceSets.collects(project.rootProject.project(input.dependency.name).projectDir.absolutePath)

        val lastModified =
            collects.maxBy { it.lastModified() }?.lastModified() ?: System.currentTimeMillis()

        if (lastModified != entry.timestamp) {
            input.status = Status.EXPIRED
            input.entry = null
            val jsonFile = File(input.target.rootDir, entry.path)
            val publicationFile = File(jsonFile.parentFile, "${entry.groupId.replace(".", "/")}/${entry.artifactId}")
            FileUtils.deleteDirectory(publicationFile)
            jsonFile.delete()
        } else {
            input.status = Status.VALID
        }

        return chain.proceed(input)

    }

}