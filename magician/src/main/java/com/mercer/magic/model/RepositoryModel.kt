package com.mercer.magic.model

/**
 * @author      ：mercer
 * @date        ：2021-01-28  20:47
 * @description ：仓库模型
 */
data class RepositoryModel(
    val groupId: String,
    val artifactId: String,
    val version: String,
    val packaging: String,
    val modelEncoding: String
)