package com.mercer.magic.enum

/**
 * @author          : mercer
 * @date            : 2021-02-06  22:20
 * @canonicalName   : com.mercer.magic.enum.Category
 * @description     : 依赖模块的类型
 */
enum class Category(
    val format: String,
    val container: String
) {

    Java("jar", "sourceSets"),

    Android("aar", "android")

}