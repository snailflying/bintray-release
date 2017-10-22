package com.aaron.gradle

import org.gradle.api.Project
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.javadoc.Javadoc

class AndroidArtifacts implements Artifacts {

    def variant
    def PropertyFinder propertyFinder

    AndroidArtifacts(variant, PropertyFinder propertyFinder) {
        this.variant = variant
        this.propertyFinder = propertyFinder
    }

    def all(String publicationName, Project project) {
        if (propertyFinder.getArchives() != null && propertyFinder.getArchives().size() > 1) {
            [propertyFinder.getArchives()[0], propertyFinder.getArchives()[1], javadocJar(project)]
        } else if (propertyFinder.getArchives() != null && propertyFinder.getArchives().size() > 0) {
            [propertyFinder.getArchives()[0], javadocJar(project)]
        } else {
            [sourcesJar(project), javadocJar(project), mainJar(project)]
        }

    }

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
        "$project.buildDir/outputs/aar/${project.name}-${variant.baseName}.aar"
    }

    def from(Project project) {
        project.components.add(AndroidLibrary.newInstance(project))
        project.components.android
    }

}
