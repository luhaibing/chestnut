package com.mercer.magic.trigger.impl

import com.mercer.magic.trigger.OnDependencyStatusTrigger
import com.mercer.magic.enum.Status
import com.mercer.magic.service.Logger
import org.gradle.api.artifacts.Dependency
import java.io.File
import javax.naming.OperationNotSupportedException

/**
 * @author          : mercer
 * @date            : 2021-02-07  13:49
 * @canonicalName   : com.mercer.magic.trigger.impl.DefaultDependencyStatusTrigger
 * @desc            :
 */
class DefaultDependencyStatusTrigger(private val logger: Logger) : OnDependencyStatusTrigger {

    override fun processDependency(status: Status, dependency: Dependency, file: File) {
        when (status) {
            Status.VALID -> {

            }
            Status.EXPIRED -> {
                deleteFile(file)
            }
            Status.LOST -> {
                if (file.isDirectory) {
                    // json 文件不存在
                } else {
                    deleteFile(file)
                    // json 文件存在,但无法解析
                }
            }
            else -> {
                // 不应该出现这种情况
                throw OperationNotSupportedException()
            }
        }
    }

    private fun deleteFile(file: File) {
        if (file.delete()) {
            logger.error("$file can not delete.")
        }
    }

}