package com.mercer.magic.trigger

/**
 * @author          : mercer
 * @date            : 2021-02-07  12:23
 * @canonicalName   : com.mercer.magic.trigger.OnComparableListener
 * @desc            : 对比是否相同
 */
interface OnComparableListener<DST, SRC> {

    fun compare(dst: DST, src: SRC): Boolean

}