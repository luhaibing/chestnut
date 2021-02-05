package com.mercer.magic

import com.mercer.magic.extension.DependencyReplace
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer

/**
 * @author          : mercer
 * @date            : 2021-02-06  00:09
 * @canonicalName   : com.mercer.magic.DependencyReplacePlugin
 * @description     : 本地模块动态替换为仓库依赖
 */
class DependencyReplacePlugin :BasicPlugin<Project, DependencyReplace>() {

}