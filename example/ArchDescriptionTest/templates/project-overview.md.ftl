${project.name}
===============

${project.description!}

## Access built in model

Let's access DSL's part of the project model:
${project.documentation.getOutputDir()}

${project.model!}

Let's see available components:
<#list project.components as c>
${c}

</#list>

## Generate diagram

<@plantuml diagram="UseCaseDiagram"> 

   @startuml

   left to right direction

<#list project.usecases as uc>
   :${uc.actor}:--(${uc.name})
</#list>

   @enduml

</@plantuml>

