@file:Suppress("DEPRECATION")

package com.mercer.magic

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.api.AndroidSourceSet
import com.android.manifmerger.ManifestMerger2
import com.android.manifmerger.MergingReport
import com.android.utils.NullLogger
import com.mercer.magic.enum.Category
import com.mercer.magic.extension.DefaultMicro
import com.mercer.magic.extension.Micro
import com.mercer.magic.model.SourceFolder
import com.mercer.magic.util.androidSourceSet
import com.mercer.magic.util.sourceSet
import org.gradle.StartParameter
import org.gradle.api.GradleException
import org.gradle.api.Project
import org.gradle.api.internal.tasks.DefaultSourceSetContainer
import java.io.File
import java.nio.charset.Charset

/**
 * @author          : mercer
 * @date            : 2021-02-06  00:18
 * @canonicalName   : com.mercer.magic.MicroPlugin
 * @description     : 微模块插件
 */
class MicroPlugin : FoundationPlugin<Project, Micro>() {

    override fun declareExtension(target: Project) {
        target.extensions.create(
            Micro::class.java, Micro.KEY, DefaultMicro::class.java, target
        )
    }

    override fun receiveExtension(target: Project): Micro {
        val extension = (target.extensions.findByName(Micro.KEY) as? DefaultMicro
            ?: throw NullPointerException("can not receive extension findByName(${Micro.KEY})"))
        extension.prepare()
        return extension
    }

    override fun afterEvaluateProceed(
        target: Project,
        extension: Micro?,
        startParameter: StartParameter
    ): Boolean {
        if (extension?.micros == null) {
            return false
        }
        return true
    }

    override fun afterEvaluateHandle(
        target: Project,
        extension: Micro?,
        startParameter: StartParameter
    ) {
        super.afterEvaluateHandle(target, extension, startParameter)
        if (extension!!.category == Category.Android) {
            handlerAndroid(target, extension)
        } else {
            handlerJava(target, extension)
        }
    }

    private fun handlerJava(project: Project, extension: Micro) {

        val mainSource = sourceSet(project)

        val sources = extension.micros

        sources
            .flatMap { it.coreFolders.map { f -> f } }
            .onEach { mainSource.java.srcDir(it) }

        sources
            .flatMap { it.resFolders.map { f -> f } }
            .onEach { mainSource.resources.srcDir(it) }

    }

    private fun handlerAndroid(project: Project, extension: Micro) {

        val androidSourceSet= androidSourceSet(project)

        val microSources = extension.micros

        microSources
            .flatMap { it.coreFolders.map { f -> f } }
            .onEach { androidSourceSet.java.srcDir(it) }

        microSources
            .flatMap { it.resFolders.map { f -> f } }
            .onEach { androidSourceSet.res.srcDir(it) }

        microSources
            .flatMap { it.assetsFolders.map { f -> f } }
            .onEach { androidSourceSet.assets.srcDir(it) }

        microSources
            .flatMap { it.jniFolders.map { f -> f } }
            .onEach { androidSourceSet.jni.srcDir(it) }

        microSources
            .flatMap { it.aidlFolders.map { f -> f } }
            .onEach { androidSourceSet.aidl.srcDir(it) }


        val manifests = microSources.filter {
            val manifest = it.manifest
            manifest != null && manifest.exists()
        }

        if (manifests.isEmpty()) {
            return
        }

        val manifestFile = mergeAndroidManifest(project, androidSourceSet, microSources)

        androidSourceSet.manifest.srcFile(manifestFile)

        val buildFiles = microSources.filter {
            val buildFile = it.buildFile
            buildFile != null && buildFile.exists()
        }

        if (buildFiles.isEmpty()) {
            return
        }

        applyBuildFiles(project, buildFiles)

    }

    private fun applyBuildFiles(project: Project, buildFiles: List<SourceFolder>) {
        // apply(from = "other.gradle.kts")
        buildFiles
            .mapNotNull { it.buildFile }
            .onEach {
                project.apply(hashMapOf("from" to it))
            }
    }


    private fun mergeAndroidManifest(
        project: Project,
        androidSourceSet: AndroidSourceSet,
        sourceFolders: List<SourceFolder>
    ): File {

        val manifest = androidSourceSet.manifest.srcFile
        val nullLogger = NullLogger()
        val invoker =
            ManifestMerger2.newMerger(manifest, nullLogger, ManifestMerger2.MergeType.APPLICATION)
        sourceFolders
            .map { it.manifest }
            .filter { it?.exists() ?: false }
            .filter { it!!.absolutePath != manifest.absolutePath }
            .onEach { invoker.addLibraryManifest(it) }
        val mergingReport: MergingReport = invoker.merge()

        if (!mergingReport.result.isSuccess) {
            mergingReport.log(nullLogger)
            throw  GradleException(mergingReport.reportString)
        }

        val mergedDocument =
            mergingReport.getMergedDocument(MergingReport.MergedManifestKind.MERGED)

        val manifestFile =
            File(project.buildDir, "${Micro.KEY}/${SourceFolder.MANIFEST}")

        if (manifestFile.exists()) {
            manifestFile.deleteOnExit()
        }

        manifestFile.parentFile.mkdirs()
        manifestFile.createNewFile()

        manifestFile.writeBytes(mergedDocument.toByteArray(Charset.defaultCharset()))

        return manifestFile

    }

}