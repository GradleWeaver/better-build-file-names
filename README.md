# Better Build File Names

When you have a very large file projects with many subprojects,
you can end up with the problem where you end up with a bunch of files all called `build.gradle` or `build.gradle.kts`.

There is a little known fact that you don't have to name all your build files `build.*`.
This plugin configures your build so that your build files are instead named the same as the project.

For example, this project is called `better-build-file-names` so our root build file is called
`better-build-file-names.gradle.kts`.

## How it works

This plugin, unlike pretty much all other Gradle plugins, is a `Settings` plugin, not a `Project` plugin.

You apply this plugin to your `settings.gradle` or `settings.gradle.kts` file.

## How to apply this plugin

### Gradle 6.0 or later

In your **`settings.gradle`** or **`settings.gradle.kts`** file, you need to declare the following:

```groovy
plugins {
  id("org.gradleweaver.plugins.better-build-file-names") version "0.0.1"
}
```

### Prior to Gradle 6.0

In your **`settings.gradle`** or **`settings.gradle.kts`** file, you need to declare the following:
```groovy
buildscript {
  repositories {
    maven {
      url = uri("https://plugins.gradle.org/m2/")
    }
  }
  dependencies {
    classpath("gradle.plugin.org.gradleweaver.plugins:better-build-file-names:0.0.1")
  }
}
// For a settings.gradle.kts file
apply(plugin = "org.gradleweaver.plugins.better-build-file-names")
// For a settings.gradle file
apply plugin: "org.gradleweaver.plugins.better-build-file-names"

// Put the remainder of your build logic here.
```

#### Limitations

Currently, this plugin can not be applied using the `plugins` syntax similar to how they can be applied to
project files. This is due to [this issue](https://github.com/gradle/gradle/issues/6710).
