package com.mercer.magic.model.enum

import java.util.*

/**
 * @author      ：mercer
 * @date        ：2021-01-28  23:05
 * @description ：表示已发布的本地依赖是否可以用
 */

enum class Status {

    /**
     * 未判定
     */
    UNKNOWN,

    /**
     * 有效,依赖文件存在,且未过期
     */
    VALID,

    /**
     * 过期,本地仓库内依赖,已不是最新
     */
    EXPIRED,

    /**
     * 遗失,可以 json 文件解析错误,文件不存
     */
    LOST;

    override fun toString(): String {
        return super.toString().toLowerCase(Locale.getDefault())
    }

}