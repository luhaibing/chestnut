@file:Suppress("MemberVisibilityCanBePrivate", "unused", "RemoveExplicitTypeArguments")

package com.mercer.magic.interceptor

/**
 * @author      ：mercer
 * @date        ：2021-01-28  17:55
 * @description ：责任链循环的关键
 */
class LoopChain<In, Out> constructor(
    private val input: In,
    private val index: Int,
    private val interceptors: List<Interceptor<In, Out>>
) : Interceptor.Chain<In, Out> {

    companion object {

        fun <In, Out> next(
            input: In, index: Int, interceptors: List<Interceptor<In, Out>>
        ): Interceptor.Chain<In, Out> {
            return LoopChain<In, Out>(input, index + 1, interceptors)
        }

        fun <In, Out> next(chain: LoopChain<In, Out>): Interceptor.Chain<In, Out> {
            return LoopChain(chain.input, chain.index + 1, chain.interceptors)
        }

        fun <In, Out> create(
            input: In, interceptors: List<Interceptor<In, Out>>
        ): Interceptor.Chain<In, Out> {
            val values: MutableList<Interceptor<In, Out>> = arrayListOf()
            values.addAll(interceptors)
            return LoopChain<In, Out>(input, 0, values)
        }

        fun <In, Out> create(
            input: In, worker: Interceptor<In, Out>, interceptors: List<Interceptor<In, Out>>
        ): Interceptor.Chain<In, Out> {
            val values: MutableList<Interceptor<In, Out>> = arrayListOf()
            values.addAll(interceptors)
            values.remove(worker)
            values.add(worker)
            return LoopChain<In, Out>(input, 0, values)
        }

        fun <In, Out> start(
            input: In, interceptors: List<Interceptor<In, Out>>
        ): Out {
            return create(input, interceptors).proceed(input)
        }

        fun <In, Out> start(
            input: In, worker: Interceptor<In, Out>, interceptors: List<Interceptor<In, Out>>
        ): Out {
            return create(input, worker, interceptors).proceed(input)
        }

    }

    constructor(input: In, interceptors: List<Interceptor<In, Out>>) : this(input, 0, interceptors)

    override fun input(): In = input

    override fun proceed(input: In): Out {
        val interceptor: Interceptor<In, Out> = interceptors[index]
        val next: Interceptor.Chain<In, Out> = next(this)
        return interceptor.intercept(next)
    }

}