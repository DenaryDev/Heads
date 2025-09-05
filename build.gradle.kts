plugins {
    id("java")
    alias(libs.plugins.spotless)
    alias(libs.plugins.pluginyml) apply false
    alias(libs.plugins.shadow) apply false
    alias(libs.plugins.runpaper) apply false
}

allprojects {
    group = "me.denarydev.heads"
    version = "1.4.0"
}

subprojects {
    apply(plugin = "java")
    apply(plugin = rootProject.libs.plugins.spotless.get().pluginId)

    dependencies {
        compileOnly(rootProject.libs.paper)
        compileOnly(rootProject.libs.lombok)
        annotationProcessor(rootProject.libs.lombok)
    }

    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(21)
        }

        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    spotless {
        java {
            target("**/me/denarydev/heads/**")

            licenseHeaderFile(rootProject.file("codestyle/copyright.txt"))

            trimTrailingWhitespace()
            leadingTabsToSpaces(4)
            endWithNewline()
        }
    }

    tasks {
        withType<JavaCompile>().configureEach {
            options.encoding = Charsets.UTF_8.name()
            options.release = 21
        }

        withType<Javadoc>().configureEach {
            options.encoding = Charsets.UTF_8.name()
        }

        withType<ProcessResources>().configureEach {
            filteringCharset = Charsets.UTF_8.name()
        }

        compileJava {
            dependsOn("spotlessApply")

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
