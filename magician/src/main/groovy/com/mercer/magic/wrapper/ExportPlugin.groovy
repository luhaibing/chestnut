package com.mercer.magic.wrapper;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * @author : mercer
 * @date : 2021-02-07  01:14
 * @canonicalName : com.mercer.magic.wrapper.ExportPlugin
 * @description :
 */
public class ExportPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getPluginManager().apply(com.mercer.magic.ExportPlugin.class);
        project.dependencies.class.metaClass.sdk { String value ->
            println value
        }
    }

}
