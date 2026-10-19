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

        register("SkinsRestorer") {
            required = false
            load = PaperPluginDescription.RelativeLoadOrder.BEFORE
        }
    }

    permissions {
        register("heads.open") {
            description = "Open the heads menu"
            default = BukkitPluginDescription.Permission.Default.FALSE
        }
        register("heads.search") {
            description = "Search heads by name and tags"
            default = BukkitPluginDescription.Permission.Default.FALSE
        }
        register("heads.playerhead") {
            description = "Get a player's head by name"
            default = BukkitPluginDescription.Permission.Default.FALSE
        }
        register("heads.reload") {
            description = "Reload the plugin configuration and database"
            default = BukkitPluginDescription.Permission.Default.FALSE
        }
        register("heads.user") {
            description = "User permission"
            default = BukkitPluginDescription.Permission.Default.TRUE
            children = listOf(
                "heads.open",
                "heads.search",
                "heads.playerhead"
            )
        }
        register("heads.admin") {
            description = "Admin permission"
            default = BukkitPluginDescription.Permission.Default.OP
            children = listOf(
                "heads.user",
                "heads.reload"
            )
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
