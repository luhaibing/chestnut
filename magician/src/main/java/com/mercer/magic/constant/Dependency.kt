package com.mercer.magic.constant

/**
 * @author      ：mercer
 * @date        ：2021-01-28  16:42
 * @description ：依赖部分插件的常量
 */
object Dependency {

    object Extensions {
        /**
         * 发布
         */
        const val PUBLISH: String = "publishDependency"

        /**
         * 置换
         */
        const val REPLACE: String = "replaceDependency"
    }

    /**
     * 默认的依赖文件夹地址
     */
    const val DEFAULT_DEPENDENCIES_PATH = "dependencies"
    const val DEFAULT_VERSION = "1.0.0"

    /**
     * 未定义
     */
    const val UNSPECIFIED = "unspecified"

    /**
     * 主要渠道
     */
    const val SOURCE_CHANNEL = "main"

}