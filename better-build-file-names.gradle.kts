import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
    id ("com.gradle.plugin-publish") version "0.9.10"
}

group = "org.gradleweaver.plugins"
version = "0.0.1"

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    compileOnly(gradleApi())

    testCompileOnly(gradleTestKit())
    fun junitJupiter(name: String, version: String = "5.2.0") =
            create(group = "org.junit.jupiter", name = name, version = version)
    testCompile(junitJupiter(name = "junit-jupiter-api"))
    testCompile(junitJupiter(name = "junit-jupiter-engine"))
    testCompile(junitJupiter(name = "junit-jupiter-params"))
    testCompile(group = "org.junit-pioneer", name = "junit-pioneer", version = "0.2.2")
}

publishing {
    repositories {
        // Work around Gradle TestKit limitations in order to allow for compileOnly dependencies
        maven {
            name = "test"
            url = uri("$buildDir/plugin-test-repository")
        }
    }

    publications {
        create<MavenPublication>("mavenJar") {
            from(components.getByName("java"))
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions.jvmTarget = "1.8"
}

tasks.withType<Test>().configureEach  {
    useJUnitPlatform()
}

val registeredPlugin = gradlePlugin.plugins.register("better-build-file-names") {
    id = "${project.group}.${project.name}"
    implementationClass = "org.gradleweaver.plugins.settings.BetterBuildFileNamesPlugin"
    description = "A Gradle plugin for handling platform-specific dependencies and releases."
}

pluginBundle {
    website = "https://github.com/gradleweaver/better-build-file-names"
    vcsUrl = "https://github.com/gradleweaver/better-build-file-names"
    tags = listOf("jlink")

    plugins {
        register("jlink-plugin") {
            id = registeredPlugin.get().id
            displayName = registeredPlugin.get().displayName
        }
    }
}

tasks {
    val publishPluginsToTestRepository by creating {
        dependsOn("publishPluginMavenPublicationToTestRepository")
    }
    val processTestResources: ProcessResources by getting
    val writeTestProperties by creating(WriteProperties::class) {
        outputFile = processTestResources.destinationDir.resolve("test.properties")
        property("version", version)
        property("kotlinVersion", KotlinVersion.CURRENT)
    }
    processTestResources.dependsOn(writeTestProperties)
    "test" {
        dependsOn(publishPluginsToTestRepository)
    }
}


// Keep at the bottom of this file
tasks.withType<Wrapper>().configureEach {
    gradleVersion = "4.10.2"
    distributionType = Wrapper.DistributionType.ALL
}
