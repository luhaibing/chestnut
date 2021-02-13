@file:Suppress("DEPRECATION")

package com.mercer.magic

import com.android.build.gradle.api.AndroidSourceSet
import com.mercer.magic.constant.PluginId
import com.mercer.magic.extension.DefaultExport
import com.mercer.magic.extension.Export
import com.mercer.magic.model.SourceFolder
import com.mercer.magic.util.androidSourceSet
import com.mercer.magic.util.sourceSet
import org.gradle.StartParameter
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet

/**
 * @author          : mercer
 * @date            : 2021-02-06  00:22
 * @canonicalName   : com.mercer.magic.ExportPlugin
 * @description     : 业务模块分离出可用于组件化通信的依赖
 */
class ExportPlugin : FoundationPlugin<Project, Export>() {

    override fun declareExtension(target: Project) {
        target.extensions.create(Export::class.java, Export.KEY, DefaultExport::class.java, target)
    }

    override fun receiveExtension(target: Project): Export {
        val extension = (target.extensions.findByName(Export.KEY) as? DefaultExport
            ?: throw NullPointerException("can not receive extension findByName(${Export.KEY})"))
        extension.prepare()
        return extension
    }

    override fun handle(target: Project, startParameter: StartParameter) {
        super.handle(target, startParameter)
        // print("$${javaClass.canonicalName} - handle")
        println("------------------------- ExportPlugin.handle -------------------------")
    }

    override fun afterEvaluateProceed(
        target: Project, extension: Export?, startParameter: StartParameter
    ): Boolean {
        if (target.pluginManager.hasPlugin(PluginId.Android.APPLICATION)) {
            return false
        }
        if (target.pluginManager.hasPlugin(PluginId.Gradle.GROOVY)) {
            return false
        }
        return super.afterEvaluateProceed(target, extension, startParameter)
    }

    override fun afterEvaluateHandle(
        target: Project, extension: Export?, startParameter: StartParameter
    ) {
        super.afterEvaluateHandle(target, extension, startParameter)
        // println("------------------------- ExportPlugin.afterEvaluateHandle ${target.tasks.size} -------------------------")

        val sourceFolder: SourceFolder = extension!!.sourceFolder

        if (sourceFolder.style and SourceFolder.FLAG_ANDROID != 0) {
            // android
            val androidSourceSet: AndroidSourceSet = androidSourceSet(target)
            sourceFolder.coreFolders.onEach {
                androidSourceSet.java.srcDir(it)
            }
            sourceFolder.resFolders.onEach {
                androidSourceSet.res.srcDir(it)
            }
            sourceFolder.aidlFolders.onEach {
                androidSourceSet.aidl.srcDir(it)
            }
            sourceFolder.jniFolders.onEach {
                androidSourceSet.jni.srcDir(it)
            }
        } else {
            // java
            val sourceSet: SourceSet = sourceSet(target)
            sourceFolder.coreFolders.onEach {
                sourceSet.java.srcDir(it)
            }
            sourceFolder.resFolders.onEach {
                sourceSet.resources.srcDir(it)
            }
        }

        // TODO: 2/13/21

//        target.tasks
        //target.tasks.withType(MergingReport.MergedManifestKind)

        // com.android.build.gradle.tasks.ProcessLibraryManifest_Decorated
        //target.tasks.findByName("processReleaseManifest").javaClass.canonicalName

        // com.android.build.gradle.tasks.ProcessMultiApkApplicationManifest_Decorated
        // com.android.build.gradle.tasks.ManifestProcessorTask
        //target.rootProject.project(":app").tasks.findByName("processReleaseManifest").javaClass.canonicalName
        //com.android.build.gradle.tasks.ProcessMultiApkApplicationManifest
        //target.rootProject.tasks//.findByName("processReleaseManifest").javaClass.canonicalName
        //target.rootProject.project(":app").tasks.findByName("processReleaseManifest")

        //0 = "processDebugManifest"
        //1 = "processReleaseManifest"
        //target.rootProject.project(":app").tasks.withType(com.android.build.gradle.tasks.ProcessMultiApkApplicationManifest::class.java).map { it.name }

        //0 = "processDebugAndroidTestManifest"
        //1 = "processDebugMainManifest"
        //2 = "processDebugManifest"
        //3 = "processReleaseMainManifest"
        //4 = "processReleaseManifest"
        //target.rootProject.project(":app").tasks.withType(com.android.build.gradle.tasks.ManifestProcessorTask::class.java).map { it.name }

        //0 = "processDebugMainManifest"
        //1 = "processReleaseMainManifest"
        //target.rootProject.project(":app").tasks.withType(com.android.build.gradle.tasks.ProcessApplicationManifest::class.java).map { it.name }
        //target.tasks.withType(com.android.build.gradle.tasks.ProcessLibraryManifest::class.java).map { it.name }

//        val processManifest: ProcessLibraryManifest =
//            target.tasks.findByName("processReleaseManifest") as ProcessLibraryManifest
//        // /Users/mercer/remote/chestnut/transaction/mine/src/main/AndroidManifest.xml
//        //processManifest.mainManifest.set(target.file("src/home/AndroidManifest.xml"))
//        //processManifest.mainManifest.javaClass.canonicalName
//        //org.gradle.api.internal.provider.DefaultProperty
//        val newManifest = target.file("src/home/AndroidManifest.xml")
//        //processManifest.mainManifest.value(newManifest)
//        newManifest

        // //target.extensions.findByName("android")
        ////
        //// val processManifest: ProcessLibraryManifest =
        ////            target.tasks.findByName("processReleaseManifest") as ProcessLibraryManifest
        //val extension = target.extensions.findByName("android") as LibraryExtension
        ////extension.sourceSets.named("main") as com.android.build.
        //extension.sourceSets.getByName("main") as? DefaultAndroidSourceSet
        //extension.sourceSets.getByName("main") as? com.android.build.api.dsl.AndroidSourceSet
        //extension.sourceSets.getByName("main") as? AndroidSourceSet

        // TODO: 2/7/21
        // 根据 gradle 开始的任务名 来 hook 任务
        // 在配置期后,执行期前 动态修改参与编译的源目录

//        val anchor1 = target.tasks.findByName("compileReleaseJavaWithJavac")
//        val anchor1Name = anchor1?.javaClass?.canonicalName
//
//        // compileReleaseLibraryResources processReleaseJavaRes
//        val anchor2 = target.tasks.findByName("compileReleaseLibraryResources")
//        val anchor2Name = anchor2?.javaClass?.canonicalName
//
//        val anchor3 = target.tasks.findByName("processReleaseJavaRes")
//        val anchor3Name = anchor3?.javaClass?.canonicalName
//
//        val anchor4 = target.tasks.findByName("processReleaseManifest")
//        val anchor4Name = anchor4?.javaClass?.canonicalName

        /*
        val javaCompiles: List<JavaCompile> = target.tasks.withType(JavaCompile::class.java)
            .filter {
                !it.name.contains("AndroidTest") && !it.name.contains("UnitTest")
            }

        val processLibraryManifests: List<ProcessLibraryManifest> =
            target.tasks.withType(ProcessLibraryManifest::class.java).toList()

        val compileLibraryResourcesTasks: List<CompileLibraryResourcesTask> =
            target.tasks.withType(CompileLibraryResourcesTask::class.java).toList()

        val processJavaResTasks: List<ProcessJavaResTask> =
            target.tasks.withType(ProcessJavaResTask::class.java).toList()
        */

        // TODO: 2/13/21
        // val javaCompile = javaCompiles.first()
        // val processLibraryManifest = processLibraryManifests.first()
        // val compileLibraryResourcesTask = compileLibraryResourcesTasks.first()
        // val processJavaResTask = processJavaResTasks.first()

    }

}