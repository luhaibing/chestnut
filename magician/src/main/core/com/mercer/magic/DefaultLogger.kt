package com.mercer.magic

import com.mercer.magic.service.Logger

/**
 * @author          : mercer
 * @date            : 2021-02-06  00:03
 * @canonicalName   : com.mercer.magic.DefaultLogger
 * @description     : 默认的日志输出实现
 */
class DefaultLogger : Logger {

    override fun verbose(any: Any) {
        TODO("Not yet implemented")
    }

    override fun debug(any: Any) {
        TODO("Not yet implemented")
    }

    override fun info(any: Any) {
        println(any.toString())
    }

    override fun warn(any: Any) {
        TODO("Not yet implemented")
    }

    override fun error(any: Any) {
        System.err.println(any.toString())
    }

    override fun assert(any: Any) {
        TODO("Not yet implemented")
    }

}