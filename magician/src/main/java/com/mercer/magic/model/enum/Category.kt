package com.mercer.magic.model.enum

/**
 * @author      ：mercer
 * @date        ：2021-01-28  23:04
 * @description ：依赖模块的类型
 */
enum class Category(
    val format: String,
    val container: String
) {

    Java("jar", "sourceSets"),

    Android("aar", "android")

}