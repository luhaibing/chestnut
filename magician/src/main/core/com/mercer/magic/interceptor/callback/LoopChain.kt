package com.mercer.magic.interceptor.callback

import com.mercer.magic.interceptor.OnNext

/**
 * @author      ：mercer
 * @date        ：2021-02-13  17:01
 * @description ：责任链循环的关键
 */
@Suppress("unused", "MemberVisibilityCanBePrivate")
class LoopChain<In, Out> private constructor(
    private val input: In,
    private val interceptors: List<Interceptor<In, Out>>,
    private val handlers: List<OnHandleCallback<Out>> = arrayListOf(),
    private val index: Int = 0
) : Interceptor.Chain<In, Out>, OnNext<Interceptor.Chain<In, Out>> {

    companion object {

        fun <In, Out> create(
            input: In, interceptors: List<Interceptor<In, Out>>, handlers: List<OnHandleCallback<Out>>
        ): LoopChain<In, Out> {

            val set = hashSetOf<OnHandleCallback<Out>>()
            set.addAll(handlers)
            val values = hashMapOf<Int, OnHandleCallback<Out>>()
            for (handler in set) {
                val entry = values[handler.priority]
                if (entry != null) {
                    throw IllegalArgumentException(
                        "priority : ${entry.priority} " +
                                "already have handler(${entry.javaClass.canonicalName})"
                    )
                }
                values[handler.priority] = handler
            }
            return LoopChain(input, interceptors.distinct(), values.values.sortedBy { it.priority })
        }

        fun <In, Out> start(
            input: In, interceptors: List<Interceptor<In, Out>>, handlers: List<OnHandleCallback<Out>>
        ) {
            create(input, interceptors, handlers).proceed(input)
        }

        fun <In, Out> start(
            input: In, realInterceptor: Interceptor<In, Out>,
            interceptors: List<Interceptor<In, Out>>, handlers: List<OnHandleCallback<Out>>
        ) {
            val values: MutableList<Interceptor<In, Out>> =
                if (interceptors is MutableList) {
                    interceptors
                } else {
                    arrayListOf()
                }
            values.remove(realInterceptor)
            values.add(realInterceptor)
            start(input, interceptors, handlers)
        }

    }

    private val interceptor: Interceptor<In, Out> by lazy { interceptors[index] }

    override fun next(): Interceptor.Chain<In, Out> {
        return LoopChain(input, interceptors, handlers, index + 1)
    }

    override fun input(): In {
        return input
    }

    override fun proceed(input: In) {
        val next = next()
        interceptor.intercept(next)
    }

    override fun interrupt(out: Out) {
        if (handlers.isEmpty()) {
            return
        }
        val iterator = handlers.iterator()
        var next: OnHandleCallback<Out>
        do {
            next = iterator.next()
            val interrupt = next.handle(out)
        } while (!interrupt && iterator.hasNext())
    }

}