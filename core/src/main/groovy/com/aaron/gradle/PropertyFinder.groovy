package com.aaron.gradle

import org.gradle.api.Project
import org.gradle.api.file.FileCollection;

class PropertyFinder {

    private final Project project
    private final PublishExtension extension


    //start archivesPath aaron
    private static final String FILE_EXTENSION_JAR = ".jar"
    private static final String FILE_EXTENSION_AAR = ".aar"
    //end

    PropertyFinder(Project project, PublishExtension extension) {
        this.extension = extension
        this.project = project
    }

    def getBintrayUser() {
        getString(project, 'bintrayUser', extension.bintrayUser)
    }

    def getBintrayKey() {
        getString(project, 'bintrayKey', extension.bintrayKey)
    }

    def getDryRun() {
        getBoolean(project, 'dryRun', extension.dryRun)
    }

    def getOverride() {
        getBoolean(project, 'override', extension.override)
    }

    def getPublishVersion() {
        getString(project, 'publishVersion', extension.publishVersion)
    }

    //start archivesPath aaron
    FileCollection getArchives() {
        getFiles(project, 'archivesPath', extension.archivesPath, extension.archivesName)
    }

    private FileCollection getFiles(Project project, String propertyName, String archivePath, String archiveName) {
        def path = project.hasProperty(propertyName) ? project.getProperty(propertyName) : archivePath
        if (path) {
            if (archiveName != null) {
                return project.fileTree(dir: path).filter {
                    (it.name.endsWith(FILE_EXTENSION_JAR) || it.name.endsWith(FILE_EXTENSION_AAR)) &&
                            isNameContains(it, archiveName)
                }
            } else if (extension.artifactId != null) {
                return project.fileTree(dir: path).filter {
                    (it.name.endsWith(FILE_EXTENSION_JAR) || it.name.endsWith(FILE_EXTENSION_AAR)) &&
                            isNameContains(it, extension.artifactId)
                }
            } else {
                return project.fileTree(dir: path).filter {
                    it.name.endsWith(FILE_EXTENSION_JAR) || it.name.endsWith(FILE_EXTENSION_AAR)
                }
            }
        } else {
            return null
        }
    }

    private static boolean isNameContains(File file, String archiveName) {
        return file.name.substring(0, file.name.length() - FILE_EXTENSION_JAR.length()).contains(archiveName)
    }
    //end

    private String getString(Project project, String propertyName, String defaultValue) {
        project.hasProperty(propertyName) ? project.getProperty(propertyName) : defaultValue
    }

    private boolean getBoolean(Project project, String propertyName, boolean defaultValue) {
        project.hasProperty(propertyName) ? Boolean.parseBoolean(project.getProperty(propertyName)) : defaultValue
    }
}
