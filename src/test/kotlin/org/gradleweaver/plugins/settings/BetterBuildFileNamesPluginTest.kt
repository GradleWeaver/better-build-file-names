package org.gradleweaver.plugins.settings

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.containsSubstring
import org.gradle.testkit.runner.TaskOutcome
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class BetterBuildFileNamesPluginTest : AbstractPluginTest() {
    @Test
    fun `can apply the plugin to the settings file`() {
        projectRoot.apply {
            settingsKotlinFile().writeText("""
                ${buildscriptBlockWithUnderTestPlugin()}

                apply(plugin = "org.gradleweaver.plugins.better-build-file-names")
            """.trimIndent())

            resolve("plugin-test.gradle.kts").createNewFile()
        }
        build("help").apply {
            assertEquals(TaskOutcome.SUCCESS, task(":help")!!.outcome)
            assertThat(output, containsSubstring("Root project 'plugin-test'"))
        }
    }

    @Test
    fun `applying the plugin first, still configures projects that are included later`() {
        projectRoot.apply {
            settingsKotlinFile().writeText("""
                ${buildscriptBlockWithUnderTestPlugin()}

                apply(plugin = "org.gradleweaver.plugins.better-build-file-names")

                include(":projectA")
                include(":projectB")
            """.trimIndent())

            resolve("plugin-test.gradle.kts").createNewFile()
            resolve("projectA").mkdir()
            resolve("projectA/projectA.gradle.kts").createNewFile()
            resolve("projectB").mkdir()
            resolve("projectB/projectB.gradle.kts").createNewFile()
        }
        build("projects").apply {
            assertEquals(TaskOutcome.SUCCESS, task(":projects")!!.outcome)
            assertThat(output, containsSubstring("Root project 'plugin-test'"))
            assertThat(output, containsSubstring("Project ':projectA'"))
            assertThat(output, containsSubstring("Project ':projectB'"))
        }
    }
}
