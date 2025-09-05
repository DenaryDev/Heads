import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    id(libs.plugins.shadow.get().pluginId)
    id(libs.plugins.runpaper.get().pluginId)
    id(libs.plugins.pluginyml.get().pluginId)
}

dependencies {
    compileOnly(libs.placeholderapi)
    compileOnly(libs.crystal.paper)

    implementation(project(":api"))
}

paper {
    name = rootProject.name
    version = rootProject.version.toString()
    website = "https://github.com/DenaryDev/Heads"
    author = "DenaryDev"

    main = "me.denarydev.heads.HeadsPlugin"

    apiVersion = "1.21"

    foliaSupported = true

    provides = listOf("HeadDatabase")

    serverDependencies {
        create("Crystal") {
            required = true
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }

        create("PlaceholderAPI") {
            required = false
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }
    }

    permissions {
        create("skyheads.admin") {
            description = "Admin permission"
            default = BukkitPluginDescription.Permission.Default.OP
            children = listOf(
                "skyheads.user"
            )
        }
        create("skyheads.user") {
            description = "User permission"
            default = BukkitPluginDescription.Permission.Default.TRUE
        }
    }
}

tasks {
    shadowJar {
        archiveClassifier = ""
        archiveBaseName = rootProject.name
        destinationDirectory = rootProject.projectDir.resolve("build/libs")
    }

    build {
        dependsOn(shadowJar)
    }

    runServer {
        minecraftVersion("1.21.8")
        runDirectory(rootProject.projectDir.resolve("run"))
        val file = rootProject.projectDir.resolve("run/server.jar")
        if (file.exists()) serverJar(file)
    }
}
