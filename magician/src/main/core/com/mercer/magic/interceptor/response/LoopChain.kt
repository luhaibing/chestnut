package com.mercer.magic.interceptor.response

import com.mercer.magic.interceptor.OnNext

/**
 * @author      ：mercer
 * @date        ：2021-02-13  17:01
 * @description ：责任链循环的关键
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class LoopChain<In, Out> private constructor(
    private val input: In,
    private val interceptors: List<Interceptor<In, Out>>,
    private val index: Int = 0
) : Interceptor.Chain<In, Out>, OnNext<Interceptor.Chain<In, Out>> {

    companion object {

        fun <In, Out> start(input: In, interceptors: List<Interceptor<In, Out>>): Out {
            return LoopChain(input, interceptors).proceed(input)
        }

        fun <In, Out> start(
            input: In, realInterceptor: Interceptor<In, Out>,
            interceptors: List<Interceptor<In, Out>>
        ): Out {
            val values: MutableList<Interceptor<In, Out>> =
                if (interceptors is MutableList) {
                    interceptors
                } else {
                    arrayListOf()
                }
            values.remove(realInterceptor)
            values.add(realInterceptor)
            return start(input, interceptors)
        }

    }

    private val interceptor by lazy { interceptors[index] }

    override fun next(): Interceptor.Chain<In, Out> {
        return LoopChain(input, interceptors, index + 1)
    }

    override fun input(): In {
        return input
    }

    override fun proceed(input: In): Out {
        val next = next()
        val interceptor = interceptors[index]
        return interceptor.intercept(next)
    }

}