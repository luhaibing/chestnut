package com.mercer.magic.wrapper;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

/**
 * @author : mercer
 * @date : 2021-02-07  01:15
 * @canonicalName : com.mercer.magic.wrapper.DependencyReplacePlugin
 * @description :
 */
public class DependencyReplacePlugin  implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        project.getPluginManager().apply(com.mercer.magic.DependencyReplacePlugin.class);
    }

}