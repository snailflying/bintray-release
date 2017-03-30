package com.aaron.gradle

import org.gradle.api.Project;

class PropertyFinder {

    private final Project project
    private final PublishExtension extension

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

    def getArchives() {
        getFile(project, 'archives', extension.archives)
    }

    private String getString(Project project, String propertyName, String defaultValue) {
        project.hasProperty(propertyName) ? project.getProperty(propertyName) : defaultValue
    }
    private File getFile(Project project, String propertyName, File defaultValue) {
        defaultValue
    }
    private boolean getBoolean(Project project, String propertyName, boolean defaultValue) {
        project.hasProperty(propertyName) ? Boolean.parseBoolean(project.getProperty(propertyName)) : defaultValue
    }

}
