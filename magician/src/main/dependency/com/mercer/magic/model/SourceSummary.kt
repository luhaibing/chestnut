package com.mercer.magic.model

/**
 * @author          : mercer
 * @date            : 2021-02-06  22:03
 * @canonicalName   : com.mercer.magic.model.SourceSummary
 * @description     : 项目资源摘要
 */
data class SourceSummary(
    // 资源路径
    val path: String,
    // 文件大小
    val length: Long,
    // 特征,由内容生成
    val trait: String,
    // 最后修改时间
    val lastModified: String,
    val timestamp: Long,
)