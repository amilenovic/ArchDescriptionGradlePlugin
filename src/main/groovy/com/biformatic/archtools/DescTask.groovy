package com.biformatic.archtools

import groovy.io.FileType

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.api.logging.LogLevel

import freemarker.template.Configuration
import freemarker.template.Template
import freemarker.template.TemplateException
import freemarker.template.TemplateExceptionHandler
import freemarker.cache.ClassTemplateLoader
import freemarker.cache.FileTemplateLoader
import freemarker.cache.TemplateLoader
import freemarker.cache.MultiTemplateLoader
import freemarker.template.Template
import freemarker.ext.beans.BeansWrapper
import freemarker.ext.beans.BeansWrapperBuilder
import java.io.File
import java.util.Scanner

/**
 * This is the main task of generating a SW project description.
 * @author amilenovic
 */
class DescTask extends DefaultTask {
    public static final String TASK_NAME = 'documentation'
    String description = 'Generates Project Description using supplied Freemarker template(s).'
    Configuration cfg = new Configuration(Configuration.VERSION_2_3_23)
    Map<String, Object> model = new HashMap<>()
    private static final String SEP = File.separator
    
    @TaskAction
    def description() {
        DescriptionPluginExtension doc = project.documentation
        File templateDir = project.file( doc.templateDir )

        if ( ! templateDir.exists() ) {
            project.logger.log(LogLevel.WARN, "'$doc.templateDir' template directory does not exist.")
            return
        }
        
        project.logger.info("Using '$doc.templateDir' as template directory.")
        FileTemplateLoader  ftl = new FileTemplateLoader(templateDir)
        ClassTemplateLoader ctl = new ClassTemplateLoader(this.getClass(), "${SEP}builtin-templates")
        TemplateLoader[] loaders = new TemplateLoader[2]
        loaders[0] = ftl
        loaders[1] = ctl
        MultiTemplateLoader mtl = new MultiTemplateLoader(loaders);
        cfg.setTemplateLoader(mtl);
//        cfg.setDirectoryForTemplateLoading(templateDir);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);   
        

        if ( ! cfg.isObjectWrapperExplicitlySet()  ) {
            // Create the builder:
            BeansWrapperBuilder builder = new BeansWrapperBuilder(Configuration.VERSION_2_3_23);
            builder.setExposeFields(true);

            // Get the singleton:
            BeansWrapper beansWrapper = builder.build();
            cfg.setObjectWrapper(beansWrapper);
            
            println cfg.getObjectWrapper()
        }
        
        File outputDir = project.file( "${project.buildDir}${SEP}${doc.outputDir}" )
        String figuresOutputDir = "${project.buildDir}${SEP}${doc.outputDir}${SEP}figures"
        if ( ! outputDir.exists() ) {
            if ( !outputDir.mkdirs() ) {
                project.logger.log(LogLevel.ERROR, \
                   "'$doc.outputDir' output directory does not exist. It cannot be created either.")
                return                
            }
            
            File figDir = project.file( figuresOutputDir )
            figDir.mkdirs()
        }
        
        model.put('project', project.properties)
        model.put('plantuml', new PlantUmlFreemarkerDirective(figuresOutputDir) )
        templateDir.eachFileRecurse(FileType.FILES) { file ->
            if ( file.name.endsWith('.ftl') ) {
                String outputFileName = "${project.buildDir}${SEP}${doc.outputDir}${SEP}" + file.name.replaceAll('.ftl', '')
                println 'output ' + outputFileName
                processSingleFile(file.name, outputFileName)
                
                if (doc.genHtml && outputFileName.endsWith('.md')) {
                    project.logger.info("Converting $outputFileName to HTML")
                    String templFileName = 'template.html.ftl'
                    String htmlOutFileName = outputFileName.replaceAll('.md', '.html')
                    genHtml(templFileName, outputFileName, htmlOutFileName)
                }
            }
            else {
                project.logger.log(LogLevel.WARN, "'$file.name' does not have '.ftl' extension and will not be processed.")
            }
        }
    }
    
    def processSingleFile(String input, String output) throws TemplateException {
        Template temp = cfg.getTemplate(input)
        Writer out = new FileWriter(output)
        
        temp.process(model, out)
        
        out.close()
    }
    
    def genHtml(String template, String inputMdFile, String output) {
        Template temp = cfg.getTemplate(template)
        
        Scanner scanner = new Scanner( new File(inputMdFile) )
        String templStr = scanner.useDelimiter("\\A").next()
        scanner.close() 

        Writer out = new FileWriter(output)
        
        Map<String, Object> m = new HashMap<>()
        m.put('title', project.name)
        m.put('md', templStr)
        
        temp.process(m, out)
        out.close()
    }  
}

