package com.mercer.magic.model

import com.mercer.magic.enum.Category
import java.net.URI

/**
 * @author          : mercer
 * @date            : 2021-02-06  22:19
 * @canonicalName   : com.mercer.magic.model.Publication
 * @description     : 发布的依赖对应的数据实体类
 */
data class Publication(
    val uri: UriModel,
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