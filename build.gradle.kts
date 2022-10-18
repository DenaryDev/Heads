plugins {
    java
    `maven-publish`
    id("com.github.johnrengelman.shadow") version "7.1.2" apply false
    id("org.cadixdev.licenser") version "0.6.1"
    id("xyz.jpenilla.run-paper") version "1.0.6" apply false
}

allprojects {
    apply(plugin = "java")
    group = "io.sapphiremc.heads"
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

    repositories {
        mavenCentral()
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
        maven("https://oss.sonatype.org/content/groups/public/")
        maven("https://repo.codemc.org/repository/maven-public/")
        maven("https://repo.songoda.com/repository/public/")
        maven("http://repo.denaryworld.ru/snapshots/") {
            isAllowInsecureProtocol = true
        }
    }

    dependencies  {
        compileOnly("io.sapphiremc.sapphire:sapphire-api:1.19.2-R0.1-SNAPSHOT")
        compileOnly("org.projectlombok:lombok:1.18.24")

        implementation("com.songoda:SongodaCore:2.6.16")

        annotationProcessor("org.projectlombok:lombok:1.18.24")
    }

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