package com.biformatic.archtools

/**
 * Represents a configuration for ArchDescription Tasks and Plugin.
 * @author amilenovic
 */
class DescriptionPluginExtension {
    String templateUrl = ''
    String templateDir = 'templates'
    String outputDir   = 'description'
    boolean genMd      = true;
    boolean genHtml    = false;
}

