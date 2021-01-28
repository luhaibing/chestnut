package com.mercer.magic.model.dependency

import com.mercer.magic.model.enum.Category

/**
 * @author      ：mercer
 * @date        ：2021-01-28  23:04
 * @description ：发布的依赖对应的数据实体类
 */
data class Publication(
    // 依赖记法
    val dependencyNotation: String,
    val groupId: String,
    val artifactId: String,
    val version: String,
    // 相对于 rootProject 的路径
    val path: String,
    // 对应的模块名
    val name: String,
    // 模块路径
    val projectDir: String,
    // 依赖包类别 [ java -> jar , android -> aar ]
    val category: Category,
    // 最后修改时间
    val lastModified: String,
    val timestamp: Long,
    val sourceSets: SourceSet,
    // 所有资源文件摘要
    val resourceSummaries: List<SourceSummary>,
)