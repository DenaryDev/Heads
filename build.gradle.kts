import com.diffplug.spotless.LineEnding

plugins {
    id("java")
    alias(libs.plugins.spotless)
    alias(libs.plugins.pluginyml) apply false
    alias(libs.plugins.shadow) apply false
    alias(libs.plugins.runpaper) apply false
}

allprojects {
    group = "me.denarydev.heads"
    version = "1.2.0"
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "com.diffplug.spotless")
    dependencies {
        compileOnly(rootProject.libs.sapphire)
        compileOnly(rootProject.libs.lombok)
        annotationProcessor(rootProject.libs.lombok)
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }

    spotless {
        java {
            target("**/ru/prostocraft/plugins/arena/**")

            trimTrailingWhitespace()
            indentWithSpaces(4)
            endWithNewline()
            lineEndings = LineEnding.UNIX
        }
    }

    tasks {
        withType<JavaCompile>().configureEach {
            options.encoding = Charsets.UTF_8.name()
            options.release.set(17)
        }
        withType<Javadoc>().configureEach {
            options.encoding = Charsets.UTF_8.name()
        }
        withType<ProcessResources>().configureEach {
            filteringCharset = Charsets.UTF_8.name()
        }

        compileJava {
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