package com.mercer.magic.interceptor.callback

/**
 * @author      ：mercer
 * @date        ：2021-02-13  17:08
 * @description ：处理回调
 */
interface OnHandleCallback<Out> {

    /**
     * 优先级
     */
    val priority: Int

    /**
     * 处理
     * @param   out 中断或者完成的处理结果
     * @return      是否向下传递
     */
    fun handle(out: Out): Boolean

}