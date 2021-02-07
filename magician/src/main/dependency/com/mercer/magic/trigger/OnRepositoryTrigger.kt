package com.mercer.magic.trigger

import java.net.URI

/**
 * @author          : mercer
 * @date            : 2021-02-07  13:47
 * @canonicalName   : com.mercer.magic.trigger.OnRepositoryListener
 * @desc            : 添加依赖对应的仓库
 */
interface OnRepositoryTrigger {

    /**
     * 先保存需要添加的仓库地址
     */
    fun request(uri: URI)

    /**
     * 添加仓库
     */
    fun inject()

}