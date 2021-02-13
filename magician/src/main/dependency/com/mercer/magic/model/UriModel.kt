package com.mercer.magic.model

import org.gradle.api.Project
import org.gradle.api.internal.file.BaseDirFileResolver
import java.lang.Exception
import java.net.URI
import javax.naming.OperationNotSupportedException

/**
 * @author          : mercer
 * @date            : 2021-02-07  04:02
 * @canonicalName   : com.mercer.magic.model.UriModel
 * @description     : URI模型
 */
open class UriModel(
    val scheme: String,
    val host: String?,
    var path: String
) {

    open fun trim(target: Project) {}

    fun toURI(rootProject: Project): URI {
        return when (scheme) {
            FILE -> {
                BaseDirFileResolver(rootProject.rootDir).resolveUri(path)
            }
            HTTP, HTTPS -> {
                BaseDirFileResolver(rootProject.rootDir).resolveUri("${scheme}://${host}${path}")
            }
            else -> {
                throw OperationNotSupportedException()
            }
        }
    }

    class FileUri(path: String) : UriModel(FILE, null, path) {
        override fun trim(target: Project) {
            path = path.substring(target.rootDir.absolutePath.length + 1)
        }
    }

    class RemoteUri(scheme: String, host: String, path: String) : UriModel(scheme, host, path)

    companion object {

        private const val FILE = "file"
        private const val HTTP = "http"
        private const val HTTPS = "https"

        fun parse(uri: URI): UriModel {
            return when (uri.scheme) {
                FILE -> {
                    FileUri(uri.path)
                }
                HTTP -> {
                    RemoteUri(HTTP, uri.host, modifyDefaultPath(uri.path))
                }
                HTTPS -> {
                    RemoteUri(HTTPS, uri.host, modifyDefaultPath(uri.path))
                }
                else -> {
                    throw Exception("can not handle this situation.")
                }
            }
        }

        /**
         * 修正
         */
        private fun modifyDefaultPath(path: String): String {
            return if (path.trim().isEmpty()) {
                "/"
            } else {
                path
            }
        }


    }

}