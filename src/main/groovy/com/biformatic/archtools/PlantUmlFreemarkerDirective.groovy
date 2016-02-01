
package com.biformatic.archtools

import freemarker.core.Environment
import freemarker.template.TemplateDirectiveBody
import freemarker.template.TemplateDirectiveModel
import freemarker.template.TemplateException
import freemarker.template.TemplateModel
import freemarker.template.TemplateModelException
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.StringWriter
import java.util.Map
import java.util.logging.Level
import java.util.logging.Logger
import net.sourceforge.plantuml.FileFormat
import net.sourceforge.plantuml.FileFormatOption
import net.sourceforge.plantuml.SourceStringReader

/**
 * Implementation of <@plantuml> Freemaker directive.
 * It takes input body that is expected to be a PlantUml string, then it generates
 * a picture and supplies a link to it back to markdown document.
 * @author amilenovic
 */
class PlantUmlFreemarkerDirective implements TemplateDirectiveModel {

    private static final Logger LOG = Logger.getLogger( PlantUmlFreemarkerDirective.class.getName() )
    private figuresDir = 'figures'
    
    public PlantUmlFreemarkerDirective() {
    }

    public PlantUmlFreemarkerDirective(String figuresDir) {
        this.figuresDir = figuresDir
    }
    
    @Override
    public void execute(Environment env, Map params, TemplateModel[] loopVars, 
            TemplateDirectiveBody body) throws TemplateException, IOException {
        if (params.isEmpty() || params.get("diagram") == null ) {
            throw new TemplateModelException(
                    "You have to supply diagram name using '<@plantuml diagram=\"some name\">'.");
        }
        if (loopVars.length != 0) {
            throw new TemplateModelException("This directive doesn't allow loop variables.");
        }
        
        // If there is non-empty nested content:
        if (body != null) {            
            StringWriter sw = new StringWriter();
            body.render(sw);            
            String source = sw.toString();
            
            SourceStringReader reader = new SourceStringReader(source);
            
            String diagram = params.get("diagram").toString();
            
            String diagramFullPath = figuresDir + File.separator + diagram + ".svg";
            LOG.log(Level.INFO, "Generating diagram: {0}", diagramFullPath);
            
            final FileOutputStream fos = new FileOutputStream(diagramFullPath);            
            reader.generateImage(fos, new FileFormatOption(FileFormat.SVG));
            fos.close();
            
            env.getOut().write("![" + diagram + "](" + diagramFullPath + ")\n");
            
            
        } else {
            throw new RuntimeException("missing body");
        }        
    }	
}

