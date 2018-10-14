

// TODO: Convert to use this plugin once it's published.
/**
 * This configures the gradle build so we can use non-standard build file names.
 * Additionally, this project can support sub-projects who's build file is written in Kotlin.
 *
 * @param project The project to configure.
 */
fun configureGradleBuild(project: ProjectDescriptor) {
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
configureGradleBuild(rootProject)
