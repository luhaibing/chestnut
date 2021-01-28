package com.mercer.magic.interceptor

import com.google.gson.Gson
import com.mercer.magic.core.interceptor.Interceptor
import com.mercer.magic.extension.dependency.DependencyReplace
import com.mercer.magic.model.dependency.DependencyEntry
import com.mercer.magic.model.dependency.Publication
import com.mercer.magic.model.enum.Status
import org.gradle.api.artifacts.Dependency

/**
 * 拦截器的永远的第一项
 * 将 .json 文件转换为 数据实体
 */
class TransformDependencyEntryInterceptor(
    private val extension: DependencyReplace
) : DependencyInterceptor {

    override fun intercept(chain: Interceptor.Chain<DependencyEntry, Dependency>): Dependency? {

        val input = chain.input()
        val srcDir = extension.srcDir

        if (!srcDir.exists()) {
            input.status = Status.LOST
            return chain.proceed(input)
        }
        val listFiles = srcDir.listFiles()?.toList()

        if (listFiles == null) {
            input.status = Status.LOST
            return chain.proceed(input)
        }

        val pair = listFiles
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

        if (pair?.second == null) {
            input.status = Status.LOST
            pair?.first?.delete()
        } else {
            input.status = Status.UNKNOWN
            input.entry = pair.second
        }
        return chain.proceed(input)

    }

}