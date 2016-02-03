Architecture Description Gradle Plugin
======================================

This is a Gradle plugin that is generating a Project or Architecture description.
The description is generating using [Freemarker](http://freemarker.org) templates.

The templates are Markdown based. They have to have '```.md.ftl```' suffix, so that
plugin can process them.

Diagraming is supported using [PlantUml](http://plantuml.com) component.

It is a Gradle model that is supplied to Freemarker template engine, so all project
properties, embedded or extended, are available for templates.

The directory containing templates and output directory are configurable.


## Usage 

In your ```build.gradle``` file, add following plugin:

``` groovy
apply plugin: com.biformatic.archtools.ArchDescription
```

By default, Templates are located in ```$rootDir/templates``` and output is in
```$buildDir/description```. Both of these can be configured:

``` groovy

    apply plugin: com.biformatic.archtools.ArchDescription
    // as example, this is java based project
    apply plugin: java

    documentation {
        templateDir = 'source-description'
        outputDir   = 'output-description'    
    }

```

### Generate Markdown files

This plugin adds ```documentation``` task to the project, which initiates the
process of generating the Project or Architecture description. In short, 
```./gradlew documentation``` will invoke this task.

As a result, in ```outputDir``` you will gave Markdown files generated.

### Generate HTML from Markdown

``` groovy

    documentation {
        templateDir = 'source-description'
        outputDir   = 'output-description'    
        genHtml     = true
    }

```

By setting ```genHtml``` to ```true```, this plugin will do another pass and convert
Markdown files to Html. The plugin uses [Strapdown.js](http://strapdownjs.com/) component
for such conversion.


## Example

Go to ```example``` directory to check out how to use.