Architecture Description Gradle Plugin
======================================

This is a Gradle plugin that is generating a Project or Architecture description.
The description is generating using [Freemarker](http://freemarker.org) templates.
It is a Gradle model that is supplied to Freemarker template engine, so all project
properties, embedded or extended, are available for templates.

The directory containing templates and output directory are configurable.


## Usage 

In your ```build.gradle``` file, add following plugin:

``` groovy
apply plugin: com.biformatic.archtools.arch-description
```

By default, Templates are located in ```$rootDir/templates``` and output is in
```$buildDir/description```. Both of these can be configured:

``` groovy
    apply plugin: com.biformatic.archtools.arch-description
    // as example, this is java based project
    apply plugin: java
    description {
        templateDir = 'source-description'
        outputDir   = 'output-description'    
    }
}
```
## Examples

### Simple reporting

### Use your DSL in templates

### Use Diagrams

### Generate HTML

### Generate PDF