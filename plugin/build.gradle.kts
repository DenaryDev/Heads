plugins {
    id("com.github.johnrengelman.shadow")
    id("xyz.jpenilla.run-paper")
}

dependencies {
    compileOnly("me.clip:placeholderapi:2.11.2")

    implementation(project(":api"))
}

tasks {
    processResources {
        filesMatching(listOf("plugin.yml", "config.yml")) {
            expand("version" to project.version)
        }
    }

    shadowJar {
        archiveClassifier.set("")
        archiveBaseName.set(rootProject.name)
        destinationDirectory.set(rootProject.projectDir.resolve("build/libs"))
        relocate("com.songoda.core", "io.sapphiremc.heads.core")
        minimize()
    }

    build {
        dependsOn(shadowJar)
    }

    runServer {
        minecraftVersion("1.19.2")
        if (!System.getenv("useCustomCore").isNullOrEmpty()) {
            serverJar.set(project.projectDir.resolve("run/server.jar"))
        }
    }
}