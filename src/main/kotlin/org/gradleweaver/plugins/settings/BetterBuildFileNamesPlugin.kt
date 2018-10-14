package org.gradleweaver.plugins.settings

import org.gradle.api.GradleException
import org.gradle.api.Plugin
import org.gradle.api.initialization.ProjectDescriptor
import org.gradle.api.initialization.Settings
import java.io.File

open class BetterBuildFileNamesPlugin: Plugin<Settings> {
    override fun apply(target: Settings) {
        configureGradleBuild(target.rootProject)
    }

    /**
     * This configures the gradle build so we can use non-standard build file names.
     * Additionally, this project can support sub-projects who's build file is written in Kotlin.
     *
     * @param project The project to configure.
     */
    private fun configureGradleBuild(project: ProjectDescriptor) {
        val projectBuildFileBaseName = project.name

        val groovyBuild = File(project.projectDir, "$projectBuildFileBaseName.gradle")
        val kotlinBuild = File(project.projectDir, "$projectBuildFileBaseName.gradle.kts")
        if (groovyBuild.exists() && kotlinBuild.exists()) {
            throw GradleException(
                    "Project ${project.name} can not have both a ${groovyBuild.name} and a ${kotlinBuild.name} file. " +
                            "Rename one so that the other can serve as the base for the project's build"
            )
        }
        project.buildFileName = when {
            groovyBuild.exists() -> groovyBuild.name
            kotlinBuild.exists() -> kotlinBuild.name
            else ->
                throw GradleException("Project `${project.name}` must have a either a file " +
                        "containing ${groovyBuild.name} or ${kotlinBuild.name}")
        }

        // Any nested children projects also get configured.
        project.children.forEach { configureGradleBuild(it) }
    }

}
