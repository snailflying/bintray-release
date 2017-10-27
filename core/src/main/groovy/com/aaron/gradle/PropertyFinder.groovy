package com.aaron.gradle

import org.gradle.api.Project
import org.gradle.api.file.FileCollection

class PropertyFinder {

    private final Project project
    private final PublishExtension extension


    private static final String FILE_EXTENSION_JAR = ".jar"
    private static final String FILE_EXTENSION_AAR = ".aar"

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

    def getPublishVersion() {
        getString(project, 'publishVersion', extension.publishVersion ?: extension.version)
    }

    FileCollection getArchives() {
        getFiles(project, 'archivesPath', extension.archivesPath, extension.archivesName)
    }

    File getArchive() {
        extension.archives
    }

    private String getString(Project project, String propertyName, String defaultValue) {
        project.hasProperty(propertyName) ? project.getProperty(propertyName) : defaultValue
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

    private boolean getBoolean(Project project, String propertyName, boolean defaultValue) {
        project.hasProperty(propertyName) ? Boolean.parseBoolean(project.getProperty(propertyName)) : defaultValue
    }

    private boolean isNameContains(File file, String archiveName) {
        return file.name.substring(0, file.name.length() - FILE_EXTENSION_JAR.length()).contains(archiveName)
    }


}
