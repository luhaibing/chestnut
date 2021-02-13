package com.mercer.magic.interceptor.response

/**
 * @author      ：mercer
 * @date        ：2021-02-13  17:01
 * @description ：拦截器
 */
interface Interceptor<In, Out> {

    /**
     * 拦截处理
     */
    fun intercept(chain: Chain<In, Out>): Out

    interface Chain<In, Out> {

        /**
         * 参数
         */
        fun input(): In

        /**
         * 继续
         */
        fun proceed(input: In): Out

    }

}