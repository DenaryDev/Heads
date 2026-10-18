import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    alias(libs.plugins.shadow)
    alias(libs.plugins.runpaper)
    alias(libs.plugins.pluginyml)
}

dependencies {
    compileOnly(libs.placeholderapi)
    compileOnly(libs.crystal.paper)

    implementation(project(":api"))
}

paper {
    name = rootProject.name
    version = project.version.toString()
    website = "https://github.com/DenaryDev/Heads"
    author = "DenaryDev"

    main = "me.denarydev.heads.HeadsPlugin"

    apiVersion = "26.3"

    foliaSupported = true

    provides = listOf("HeadDatabase")

    serverDependencies {
        register("Crystal") {
            required = true
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }

        register("PlaceholderAPI") {
            required = false
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }
    }

    permissions {
        register("skyheads.admin") {
            description = "Admin permission"
            default = BukkitPluginDescription.Permission.Default.OP
            children = listOf(
                "skyheads.user"
            )
        }
        register("skyheads.user") {
            description = "User permission"
            default = BukkitPluginDescription.Permission.Default.TRUE
        }
    }
}

tasks {
    shadowJar {
        archiveClassifier = ""
        archiveBaseName = rootProject.name
        destinationDirectory = layout.settingsDirectory.dir("build/libs")
    }

    runServer {
        minecraftVersion("26.3")
        runDirectory = layout.settingsDirectory.dir("run")

        val file = layout.settingsDirectory.file("run/server.jar").asFile
        if (file.exists()) serverJar(file)
    }
}
