package com.mercer.magic.trigger.repository

import com.mercer.magic.trigger.OnRepositoryTrigger
import org.gradle.api.Project
import java.net.URI

/**
 * @author          : mercer
 * @date            : 2021-02-07  13:49
 * @canonicalName   : com.mercer.magic.trigger.repository.DefaultRepositoryListener
 * @desc            :
 */
class DefaultRepositoryTrigger(private val project: Project) : OnRepositoryTrigger {

    private val uris: MutableMap<String, URI> = hashMapOf()

    override fun request(uri: URI) {
        val key = uri.toString()
        if (uris[key] == null) {
            uris[key] = uri
        }
    }

    override fun inject() {
        uris.onEach { entry ->
            project.repositories.maven { it ->
                it.url = entry.value
            }
        }
    }

}