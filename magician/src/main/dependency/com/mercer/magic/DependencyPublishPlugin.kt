package com.mercer.magic

import com.mercer.magic.extension.DependencyPublish
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionContainer

/**
 * @author          : mercer
 * @date            : 2021-02-06  00:08
 * @canonicalName   : com.mercer.magic.DependencyPublishPlugin
 * @description     : 模块发布依赖到本地仓库
 */
class DependencyPublishPlugin : BasicPlugin<Project, DependencyPublish>() {

}