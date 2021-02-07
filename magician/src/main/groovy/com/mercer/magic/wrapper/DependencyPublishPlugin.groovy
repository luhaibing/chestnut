package com.mercer.magic.wrapper;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * @author : mercer
 * @date : 2021-02-07  01:12
 * @canonicalName : com.mercer.magic.wrapper.DependencyPublishPlugin
 * @description :
 */
public class DependencyPublishPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getPluginManager().apply(com.mercer.magic.DependencyPublishPlugin.class);
    }

}
