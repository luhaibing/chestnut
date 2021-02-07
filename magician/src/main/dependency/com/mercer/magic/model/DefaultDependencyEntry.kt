package com.mercer.magic.model

import com.mercer.magic.enum.Status
import org.gradle.api.Project
import org.gradle.api.artifacts.Dependency
import java.io.File

/**
 * @author          : mercer
 * @date            : 2021-02-07  10:51
 * @canonicalName   : com.mercer.magic.model.DefaultDependencyEntry
 * @description     :
 */
data class DefaultDependencyEntry(
    override val target: Project,
    override val dependency: Dependency,
    override val configurations: List<String>,
) : DependencyEntry {

    override lateinit var status: Pair<File, Status>

    override var entry: Publication? =null

}