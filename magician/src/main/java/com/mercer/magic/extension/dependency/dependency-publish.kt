@file:Suppress("unused")

package com.mercer.magic.extension.dependency

import com.mercer.magic.constant.Dependency
import com.mercer.magic.core.function.OnAnchorFunction
import com.mercer.magic.core.function.OnRepositoryFunction
import com.mercer.magic.function.anchor.MavenPublishAnchorFunctionImpl
import com.mercer.magic.function.repository.MavenRepositoryFunctionImpl
import com.mercer.magic.model.enum.Repository
import org.gradle.api.Project
import org.gradle.api.Task
import java.io.File

/**
 * @author      ：mercer
 * @date        ：2021-01-29  01:00
 * @description ：项目模块生成仓库依赖
 */
interface DependencyPublish {

    // 输出
    var output: File

    // 组
    var groupId: String

    // 模块名
    var artifactId: String

    // 版本号
    var version: String

    /**
     * 任务锚点
     */
    var anchor: Task?

    fun anchor(value: String)

    /**
     * 默认设置
     */
    fun defaultSetting()

    /**
     * 支持的仓库的任务锚点
     */
    val repositories: List<OnAnchorFunction>

    /**
     * 添加支持的仓库
     */
    fun addRepository(vararg values: OnAnchorFunction)
    fun addRepository(values: List<OnAnchorFunction>)

    var repository: OnAnchorFunction

}

open class DependencyPublishImpl constructor(
    private val target: Project,
    final override val repositories: MutableList<OnAnchorFunction> = arrayListOf()
) : DependencyPublish {

    init {
        repositories.add(MavenRepositoryFunctionImpl())
        repositories.add(MavenPublishAnchorFunctionImpl())
    }

    override var output: File = File(target.rootDir, Dependency.DEFAULT_DEPENDENCIES_PATH)

    override var groupId: String = target.group.toString()

    override var artifactId: String = target.name

    override var version: String = target.version.toString()

    override var anchor: Task? = null
        set(value) {
            changeRepository(value?.name)
            field = value
        }

    private fun changeRepository(value: String?) {
        repository = when (value) {
            null, Repository.MAVEN.anchor -> {
                repositories[0]
            }
            Repository.MAVEN_PUBLISH.anchor -> {
                repositories[1]
            }
            else -> {
                throw NullPointerException("can not discern anchor by name(${value}).")
            }
        }
    }

    override fun anchor(value: String) {
        changeRepository(value)
        anchor = target.tasks.findByName(value)
            ?: throw NullPointerException("can not found task by name(${value}).")
    }

    override fun defaultSetting() {
        if (groupId == target.rootProject.name && target.rootProject.group.toString()
                .isNotEmpty()
        ) {
            groupId = target.rootProject.group.toString()
        }
        version = if (version == Dependency.UNSPECIFIED &&
            target.rootProject.version.toString() != Dependency.UNSPECIFIED
        ) {
            target.rootProject.version.toString()
        } else {
            Dependency.DEFAULT_VERSION
        }
        if (anchor == null) {
            (repository as OnRepositoryFunction).apply(target, this)
            anchor = repository.acquire(target)
        }
    }

    override fun addRepository(vararg values: OnAnchorFunction) {
        addRepository(values.toList())
    }

    override fun addRepository(values: List<OnAnchorFunction>) {
        repositories.addAll(values)
    }

    override var repository: OnAnchorFunction = repositories[0]

}