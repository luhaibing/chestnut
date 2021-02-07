package com.mercer.magic.service

/**
 * @author          : mercer
 * @date            : 2021-02-06  23:00
 * @canonicalName   : com.mercer.magic.service.OnAcquireListener
 * @description     : 简单回调获取热值(最新时间点的值)
 */
interface OnAcquireListener<T> {

    fun acquire(): T

}