package org.gradleweaver.plugins.settings

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
        }
    }
}
