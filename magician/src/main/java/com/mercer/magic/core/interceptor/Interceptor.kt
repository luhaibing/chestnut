package com.mercer.magic.core.interceptor

/**
 * @author      ：mercer
 * @date        ：2021-01-28  17:53
 * @description ：拦截器
 */
interface Interceptor<In, Out> {

    /**
     * 拦截处理
     */
    fun intercept(chain: Chain<In, Out>): Out?

    /**
     * 拦截器责任链
     */
    interface Chain<In, Out>{

        /**
         * 输入
         */
        fun input(): In

        /**
         * 处理
         */
        fun proceed(input: In): Out?

    }

}