package com.mercer.magic.enum

/**
 * @author          : mercer
 * @date            : 2021-02-06  15:54
 * @canonicalName   : com.mercer.magic.enum.RepositoryPlugin
 * @description     : 仓库插件常量
 */
enum class RepositoryPlugin (
    val id: String,
    val anchor: String
) {

    MAVEN("maven", "uploadArchives"),

    MAVEN_PUBLISH("maven-publish", "publishHelloPublicationToMavenRepository"),

}