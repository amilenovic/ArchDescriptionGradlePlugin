package com.biformatic.archtools

import java.io.PrintWriter

import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import static org.junit.Assert.*

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

/**
 * DescTask Unit tests.
 * TODO: align on path separator, currently works on *nix only.
 * @author amilenovic
 */
class DescTaskTest {
    
    Project project = null
    DescTask task = null

    public DescTaskTest() {
    }

    @Before
    public void setUp() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'com.biformatic.archtools.arch-description'             
        task = project.tasks.documentation
    }
    
    @After
    public void tearDown() {
        // to clean up tmp directory created by ProjectBuilder
        project.delete( project.rootDir )
        task = null
        project = null
    }

    @Test
    public void noTemplateDir() {
        task.description()
        assertFalse project.file( "${project.buildDir}/${project.documentation.outputDir}").exists()
    }
    
    @Test
    public void noOutputDir() {
        boolean tempDirCreated = project.file( project.documentation.templateDir ).mkdir()
        assertTrue tempDirCreated
        
        PrintWriter out = new PrintWriter ( "${project.rootDir}/${project.documentation.templateDir}/test.md.ftl" )        
        out.println '${project.name} Description'
        out.close()
        
        task.description()        
        
        assertTrue project.file( "${project.buildDir}/${project.documentation.outputDir}").exists()
    }
    
    @Test(expected=freemarker.core.ParseException.class)
    public void wrongTemplateSyntax() {
        boolean tempDirCreated = project.file( project.documentation.templateDir ).mkdir()
        assertTrue tempDirCreated
        
        PrintWriter out = new PrintWriter ( "${project.rootDir}/${project.documentation.templateDir}/test.md.ftl" )        
        out.println '${project.name Description'
        out.close()

        task.description()                
        
        assertFalse project.file( "${project.buildDir}/${project.documentation.outputDir}/test.md").exists()        
    }
 
    @Test(expected=freemarker.template.TemplateException.class)
    public void wrongTemplate() {
        boolean tempDirCreated = project.file( project.documentation.templateDir ).mkdir()
        assertTrue tempDirCreated
        
        PrintWriter out = new PrintWriter ( "${project.rootDir}/${project.documentation.templateDir}/test.md.ftl" )        
        out.println '${project.nameeeee} Description'
        out.close()

        task.description()                

        assertFalse project.file( "${project.buildDir}/${project.documentation.outputDir}/test.md").exists()        
    }

    @Test
    public void wrongTemplateName() {
        boolean tempDirCreated = project.file( project.documentation.templateDir ).mkdir()
        assertTrue tempDirCreated
        
        PrintWriter out = new PrintWriter ( "${project.rootDir}/${project.documentation.templateDir}/test.md.wrongext" )        
        out.println '${project.name Description'
        out.close()

        task.description()     
        
        assertTrue project.file( "${project.buildDir}/${project.documentation.outputDir}").list().length == 0        
    }    
    
    @Test
    public void noTemplates() {
        boolean tempDirCreated = project.file( project.documentation.templateDir ).mkdir()
        assertTrue tempDirCreated
        task.description()
        assertTrue project.file( "${project.buildDir}/${project.documentation.outputDir}").list().length == 0     
    }
 
    @Test
    public void multipleTemplates() {
        boolean tempDirCreated = project.file( project.documentation.templateDir ).mkdir()
        assertTrue tempDirCreated
        
        PrintWriter out = new PrintWriter ( "${project.rootDir}/${project.documentation.templateDir}/test1.md.ftl" )        
        out.println '${project.name} Description'
        out.close()
        out = new PrintWriter ( "${project.rootDir}/${project.documentation.templateDir}/test2.md.ftl" )        
        out.println '${project.name} Description'
        out.close()
        
        task.description() 
        
        assertTrue project.file( "${project.buildDir}/${project.documentation.outputDir}/test1.md").exists()        
        assertTrue project.file( "${project.buildDir}/${project.documentation.outputDir}/test2.md").exists()        
    }    
    
}
