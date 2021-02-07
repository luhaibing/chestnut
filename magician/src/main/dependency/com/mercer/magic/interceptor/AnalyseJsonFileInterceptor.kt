package com.mercer.magic.interceptor

import com.google.gson.Gson
import com.mercer.magic.enum.Status
import com.mercer.magic.extension.DependencyReplace
import com.mercer.magic.model.DefaultDependencyEntry
import com.mercer.magic.model.DependencyEntry
import com.mercer.magic.model.Publication
import org.gradle.api.artifacts.Dependency
import java.io.File

/**
 * @author          : mercer
 * @date            : 2021-02-07  10:41
 * @canonicalName   : com.mercer.magic.interceptor.AnalyseFileInterceptor
 * @description     : 解析 json文件 生成对应数据模型
 */
class AnalyseJsonFileInterceptor(private val extension: DependencyReplace) : DependencyInterceptor {

    override fun intercept(chain: Interceptor.Chain<DependencyEntry, Dependency>): Dependency {

        val input: DefaultDependencyEntry = chain.input() as DefaultDependencyEntry

        val folder = extension.input

        if (!folder.exists()) {
            input.status = Pair(folder, Status.LOST)
            return chain.proceed(input)
        }

        val listFiles = folder.listFiles()?.toList()

        if (listFiles == null) {
            input.status = Pair(folder, Status.LOST)
            return chain.proceed(input)
        }

        val pair: Pair<File, Publication?>? = listFiles
            .filter { it.exists() }
            .filter { it.name.endsWith(".json") }
            .map {
                try {
                    it to Gson().fromJson(String(it.readBytes()), Publication::class.java)
                } catch (e: Exception) {
                    it to null
                }
            }
            .find {
                it.second?.name == input.dependency.name
            }

        if (pair == null) {
            input.status = Pair(folder, Status.LOST)
            return chain.proceed(input)
        }

        val file = pair.first
        val publication = pair.second

        if (publication == null) {
            input.status = Pair(file, Status.LOST)
        } else {
            input.status = Pair(file, Status.UNDECIDED)
            input.entry = publication
        }
        return chain.proceed(input)
    }


}