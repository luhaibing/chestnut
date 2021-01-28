package com.mercer.magic.core.function

import com.mercer.magic.extension.dependency.DependencyPublish
import org.gradle.api.Project

/**
 * @author      ：mercer
 * @date        ：2021-01-28  22:34
 * @description ：仓库上传任务回调
 */
interface OnRepositoryFunction : OnAnchorFunction {

    /**
     * 应用插件,添加发布任务
     */
    fun apply(target: Project, extension: DependencyPublish)

}