package com.biformatic.archtools

import org.junit.After
import org.junit.AfterClass
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test
import static org.junit.Assert.*

import org.gradle.api.Project
import org.gradle.testfixtures.ProjectBuilder

/**
 *
 * @author amilenovic
 */
class ArchDescriptionGradlePluginTest {

    Project project = null
    
    public ArchDescriptionGradlePluginTest() {
    }

    @Before
    public void setUp() {
        project = ProjectBuilder.builder().build()
        project.pluginManager.apply 'com.biformatic.archtools.ArchDescription'        
    }

    @After
    public void tearDown() {
        // to clean up tmp directory created by ProjectBuilder
        project.delete( project.rootDir )
        project = null
    }    
    
    @Test
    public void addTasksToProject() {
        assertNotNull project.tasks['documentation'] 
        assertTrue project.tasks.documentation instanceof DescTask
    }
    
    @Test
    public void ConfigNotSupplied() {
        assertNotNull project.documentation
        assertEquals '', project.documentation.templateUrl
        assertEquals 'templates',   project.documentation.templateDir
        assertEquals 'description', project.documentation.outputDir
        assertTrue  project.documentation.genMd
        assertFalse project.documentation.genHtml
    }
}
