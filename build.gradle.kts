plugins {
    `java-library`
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "8.1.0" apply false
    id("xyz.jpenilla.run-paper") version "2.0.1" apply false
}

allprojects {
    apply(plugin = "java-library")
    group = "me.denarydev.heads"
    version = "1.0.0"

    tasks {
        withType<ProcessResources> {
            filteringCharset = Charsets.UTF_8.name()
        }
        withType<JavaCompile> {
            options.encoding = Charsets.UTF_8.name()
            options.release.set(17)
        }
        withType<Javadoc> {
            options.encoding = Charsets.UTF_8.name()
        }

        java {
            toolchain {
                languageVersion.set(JavaLanguageVersion.of(17))
            }
        }
    }
}

subprojects {
    apply(plugin = "maven-publish")

    publishing {
        repositories {
            maven {
                name = "SapphireMC"
                url = uri("http://repo.denaryworld.ru/snapshots/")
                isAllowInsecureProtocol = true
                credentials(PasswordCredentials::class)
            }
        }
    }

    tasks {
        compileJava {
            options.encoding = Charsets.UTF_8.name()
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