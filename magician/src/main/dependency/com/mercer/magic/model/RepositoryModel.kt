package com.mercer.magic.model

/**
 * @author          : mercer
 * @date            : 2021-02-06  15:59
 * @canonicalName   : com.mercer.magic.model.RepositoryModel
 * @description     : 仓库模型
 */
data class RepositoryModel(
    val groupId: String,
    val artifactId: String,
    val version: String,
    val packaging: String,
    val modelEncoding: String
)