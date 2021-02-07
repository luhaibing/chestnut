package com.mercer.magic.trigger

import com.mercer.magic.extension.DefaultDependencyPublish
import com.mercer.magic.extension.DependencyPublish
import com.mercer.magic.service.OnAcquireListener
import java.net.URI
import java.security.cert.Extension

/**
 * @author          : mercer
 * @date            : 2021-02-06  14:40
 * @canonicalName   : com.mercer.magic.trigger.OnAnchorProvider
 * @description     : 上传动作实现
 */
interface OnRepositoryProvider : OnAnchorTrigger {

    val url: URI?

    fun url(value: String): URI

    val groupId: String

    val artifactId: String

    val version: String

    fun prepare(extension: DependencyPublish)

    fun apply()

}