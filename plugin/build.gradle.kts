plugins {
    id("com.github.johnrengelman.shadow")
    id("xyz.jpenilla.run-paper")
}

dependencies {
    compileOnly(libs.bukkit)
    compileOnly(libs.papi)

    compileOnly(libs.lombok)

    implementation(project(":api"))
    implementation(libs.bundles.crystal)

    annotationProcessor(libs.lombok)
}

tasks {
    processResources {
        filteringCharset = Charsets.UTF_8.name()
        filesMatching("plugin.yml") {
            expand("version" to project.version)
        }
    }

    shadowJar {
        archiveClassifier.set("")
        archiveBaseName.set(rootProject.name)
        destinationDirectory.set(rootProject.projectDir.resolve("build/libs"))
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        relocate("me.denarydev.crystal", "me.denarydev.heads.crystal")
        relocate("com.songoda.core", "me.denarydev.heads.core")
        minimize()
    }

    build {
        dependsOn(shadowJar)
    }

    runServer {
        minecraftVersion("1.19.4")
        runDirectory(rootProject.projectDir.resolve("run/"))
        if (!System.getenv("useCustomCore").isNullOrEmpty()) {
            serverJar(rootProject.projectDir.resolve("run/server.jar"))
        }
    }
}