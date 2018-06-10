package com.aaron.gradle

import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.javadoc.Javadoc

class AndroidArtifacts implements Artifacts {

    def variant

    AndroidArtifacts(variant) {
        this.variant = variant
    }

    //start archivesPath aaron
//    def all(String publicationName, Project project) {
//        [sourcesJar(project), javadocJar(project), mainJar(project)]
//    }
    def all(String publicationName, Project project) {
        PropertyFinder propertyFinder = new PropertyFinder(project, project.publish)
        if (propertyFinder.getArchives() != null && propertyFinder.getArchives().size() > 0) {
            def archive = propertyFinder.getArchives().asList()
            archive.add(javadocJar(project))
            return archive
        } else {
            [sourcesJar(project), javadocJar(project), mainJar(project)]
        }

    }
    //end

    def sourcesJar(Project project) {
        project.task(variant.name + 'AndroidSourcesJar', type: Jar) {
            classifier = 'sources'
            variant.sourceSets.each {
                from it.java.srcDirs
            }
        }
    }

    def javadocJar(Project project) {
        def androidJavadocs = project.task(variant.name + 'AndroidJavadocs', type: Javadoc) {
            variant.sourceSets.each {
                delegate.source it.java.srcDirs
            }
            classpath += project.files(project.android.getBootClasspath().join(File.pathSeparator))
            classpath += variant.javaCompile.classpath
            classpath += variant.javaCompile.outputs.files
        }

        project.task(variant.name + 'AndroidJavadocsJar', type: Jar, dependsOn: androidJavadocs) {
            classifier = 'javadoc'
            from androidJavadocs.destinationDir
        }
    }

    def mainJar(Project project) {
        def archiveBaseName = project.hasProperty("archivesBaseName") ? project.getProperty("archivesBaseName") : project.name
        "$project.buildDir/outputs/aar/$archiveBaseName-${variant.baseName}.aar"
    }

    def from(Project project) {
        project.components.add(new AndroidLibrary(project))
        project.components.android
    }

}
