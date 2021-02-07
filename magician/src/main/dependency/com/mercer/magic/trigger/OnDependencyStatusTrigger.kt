package com.mercer.magic.trigger

import com.mercer.magic.enum.Status
import org.gradle.api.artifacts.Dependency
import java.io.File

/**
 * @author          : mercer
 * @date            : 2021-02-07  11:17
 * @canonicalName   : com.mercer.magic.trigger.DependencyStatusListener
 * @desc            : 依赖状态的监听器
 */
interface OnDependencyStatusTrigger {

    /**
     * 处理失效的仓库依赖对应的 json 文件
     * 如果 json 文件不存在,会默认回传 存放json 文件的源目录
     */
    fun processDependency(status: Status, dependency: Dependency, file: File)

}