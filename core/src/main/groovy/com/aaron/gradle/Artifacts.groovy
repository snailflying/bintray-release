package com.aaron.gradle;

import org.gradle.api.Project

interface Artifacts {

    def all(String publicationName, Project project)

}
