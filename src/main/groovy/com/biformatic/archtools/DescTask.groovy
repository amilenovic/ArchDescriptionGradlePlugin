package com.biformatic.archtools

import groovy.io.FileType

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.logging.LogLevel

import freemarker.template.Configuration
import freemarker.template.Template
import freemarker.template.TemplateException
import freemarker.template.TemplateExceptionHandler

/**
 * This is the main task of generating a SW project description.
 * TODO: align on path separator, currently works on *nix only.
 * @author amilenovic
 */
class DescTask extends DefaultTask {
    public static final String TASK_NAME = 'documentation'
    String description = 'Generates Project Description using supplied Freemarker template(s).'
    Configuration cfg = new Configuration(Configuration.VERSION_2_3_23)
    
    @TaskAction
    def description() {
        DescriptionPluginExtension doc = project.documentation
        File templateDir = project.file( doc.templateDir )

        if ( ! templateDir.exists() ) {
            project.logger.log(LogLevel.WARN, "'$doc.templateDir' template directory does not exist.")
            return
        }
        
        project.logger.info("Using '$doc.templateDir' as template directory.")
        cfg.setDirectoryForTemplateLoading(templateDir);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        
        File outputDir = project.file( "${project.buildDir}/${doc.outputDir}" )
        if ( ! outputDir.exists() ) {
            if ( !outputDir.mkdirs() ) {
                project.logger.log(LogLevel.ERROR, \
                   "'$doc.outputDir' output directory does not exist. It cannot be created either.")
                return                
            }
        }
        
        templateDir.eachFileRecurse(FileType.FILES) { file ->
            if ( file.name.endsWith('.ftl') ) {
                String outputFileName = "${project.buildDir}/${doc.outputDir}/" + file.name.replaceAll('.ftl', '')
                println 'output ' + outputFileName
                processSingleFile(file.name, outputFileName)
            }
            else {
                project.logger.log(LogLevel.WARN, "'$file.name' does not have '.ftl' extension and will not be processed.")
            }
        }
    }
    
    def processSingleFile(String input, String output) throws TemplateException {
        Template temp = cfg.getTemplate(input)
        Writer out = new FileWriter(output)
        
        Map<String, Object> m = new HashMap<>()
        m.put('project', project.properties)
        temp.process(m, out)
        
        out.close()
    }
    
    def genHtml(String input, String output) {
        
    }
}

