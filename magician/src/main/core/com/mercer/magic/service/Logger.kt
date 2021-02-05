package com.mercer.magic.service

/**
 * @author          : mercer
 * @date            : 2021-02-06  00:01
 * @canonicalName   : com.mercer.magic.service.Logger
 * @description     : 日志输出
 */
interface Logger {

    fun verbose(any: Any)

    fun debug(any: Any)

    fun info(any: Any)

    fun warn(any: Any)

    fun error(any: Any)

    fun assert(any: Any)

}