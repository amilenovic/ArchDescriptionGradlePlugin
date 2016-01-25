package com.biformatic.archtools

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * ArchDescription Plugin is responsible to generate arch description out of 
 * gradle project.
 * 
 * It takes the project model and pass it to freemaker template engine. 
 * Templates are Markdown files that accept freemaker tags.
 * 
 * The outputs are:
 *   - markdown files
 *   - HTML
 * 
 * It is possible to prepare templates either in a supplied folder or from a
 * git repo.
 * 
 * @author amilenovic
 */
class ArchDescriptionGradlePlugin implements Plugin<Project> {
    void apply(Project project) {
        project.extensions.create('documentation', DescriptionPluginExtension)
        project.task(DescTask.TASK_NAME, type: DescTask)
    }
}