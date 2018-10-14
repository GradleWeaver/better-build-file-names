package org.gradleweaver.plugins.settings

import org.gradle.testkit.runner.BuildResult
import org.gradle.testkit.runner.GradleRunner
import org.gradle.util.TextUtil
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.extension.ExtendWith
import org.junitpioneer.jupiter.TempDirectory
import java.io.File
import java.nio.file.Path
import java.util.*


@ExtendWith(TempDirectory::class)
abstract class AbstractPluginTest {

    private lateinit var tempDir: Path

    @BeforeEach
    fun beforeEach(@TempDirectory.TempDir tempDir: Path) {
        this.tempDir = tempDir
    }

    val projectRoot: File
        get() = tempDir.toFile().resolve("plugin-test").apply { mkdirs() }

    private val testRepositoryPath
        get() = TextUtil.normaliseFileSeparators(File("build/plugin-test-repository").absolutePath)

    private val testProperties: Properties by lazy {
        javaClass.getResourceAsStream("/test.properties").use {
            Properties().apply { load(it) }
        }
    }

    protected fun buildscriptBlockWithUnderTestPlugin() =
            """
        buildscript {
            repositories { maven { setUrl("$testRepositoryPath") } }
            dependencies {
                classpath("org.gradleweaver.plugins:better-build-file-names:${testProperties["version"]}")
            }
        }
        """.trimIndent()

    fun File.settingsKotlinFile() = resolve("settings.gradle.kts")

    protected fun build(vararg arguments: String): BuildResult =
            gradleRunnerFor(*arguments).forwardOutput().build()

    protected fun buildAndFail(vararg arguments: String): BuildResult =
            gradleRunnerFor(*arguments).forwardOutput().buildAndFail()

    protected fun gradleRunnerFor(vararg arguments: String): GradleRunner =
            GradleRunner.create()
                    .withProjectDir(projectRoot)
                    .withArguments(arguments.toList())
}
