package com.mercer.magic.trigger.impl

import com.mercer.magic.trigger.OnComparableListener
import com.mercer.magic.model.SourceSummary
import java.io.File

/**
 * @author          : mercer
 * @date            : 2021-02-07  12:25
 * @canonicalName   : com.mercer.magic.trigger.impl.DefaultComparableListener
 * @desc            : 对比参与编译的资源目录是否发生改变
 */
class DefaultComparableListener : OnComparableListener<List<SourceSummary>, List<File>> {

    override fun compare(dst: List<SourceSummary>, src: List<File>): Boolean {

        if (dst.isEmpty() && src.isEmpty()) {
            return true
        }

        if (dst.isEmpty() || src.isEmpty()) {
            return false
        }

        if (dst.size != src.size) {
            return false
        }

        val dstLast: SourceSummary = dst.maxBy { it.timestamp }!!
        val srcLast: File = src.maxBy { it.lastModified() }!!

        val dstLastModified = dstLast.timestamp
        val srcLastModified = srcLast.lastModified()

        return dstLastModified == srcLastModified

    }

}