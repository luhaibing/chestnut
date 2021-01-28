package com.mercer.magic.model.enum

/**
 * @author      ：mercer
 * @date        ：2021-01-29  01:30
 * @description ：仓库
 */
enum class Repository(
    val id: String,
    val anchor: String
) {

    MAVEN("maven", "uploadArchives"),

    MAVEN_PUBLISH("maven-publish", "publishHelloPublicationToMavenRepository"),

}