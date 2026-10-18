import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
    id("java")
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.pluginyml) apply false
    alias(libs.plugins.shadow) apply false
    alias(libs.plugins.runpaper) apply false
}

allprojects {
    group = providers.gradleProperty("projectGroup").get()
    version = providers.gradleProperty("projectVersion").get()
}

subprojects {
    apply(plugin = "java-library")
    apply(plugin = "com.diffplug.spotless")

    dependencies {
        "compileOnly"(rootProject.libs.paper)
        "compileOnly"(rootProject.libs.lombok)
        "annotationProcessor"(rootProject.libs.lombok)
    }

    configure<JavaPluginExtension> {
        toolchain.languageVersion = JavaLanguageVersion.of(25)
    }

    configure<SpotlessExtension> {
        java {
            target("**/me/denarydev/heads/**")

            licenseHeaderFile(layout.settingsDirectory.file("codestyle/copyright.txt"))

            trimTrailingWhitespace()
            leadingTabsToSpaces(4)
            endWithNewline()
        }
    }

    tasks {
        withType<JavaCompile>().configureEach {
            options.compilerArgs.addAll(
                listOf(
                    "-parameters",
                    "-nowarn",
                    "-Xlint:-unchecked",
                    "-Xlint:-deprecation",
                    "-Xlint:-processing"
                )
            )
            options.isFork = true
        }
    }
}
