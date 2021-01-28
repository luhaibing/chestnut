package com.mercer.magic.model.dependency

/**
 * @author      ：mercer
 * @date        ：2021-01-28  23:01
 * @description ：项目资源摘要
 */
data class SourceSummary(
    // 资源路径
    val path: String,
    // 特征,由内容生成
    val trait: String,
    // 最后修改时间
    val lastModified: String,
    val timestamp: Long,
    // 文件大小
    val length: Long
)
