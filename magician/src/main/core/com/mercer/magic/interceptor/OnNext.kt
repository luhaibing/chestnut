package com.mercer.magic.interceptor

/**
 * @author      ：mercer
 * @date        ：2021-02-13  17:03
 * @description ：获取下一个责任处理者
 */
interface OnNext<Chain> {

    fun next(): Chain

}