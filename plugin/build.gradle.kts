import net.minecrell.pluginyml.bukkit.BukkitPluginDescription
import net.minecrell.pluginyml.paper.PaperPluginDescription
import xyz.jpenilla.runpaper.task.RunServer

plugins {
    id(libs.plugins.shadow.get().pluginId)
    id(libs.plugins.runpaper.get().pluginId)
    id(libs.plugins.pluginyml.get().pluginId)
}

dependencies {
    compileOnly(libs.placeholderapi)

    implementation(project(":api"))
    implementation(libs.bundles.crystal)
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

runPaper {
    folia.registerTask()
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

    withType<RunServer>().configureEach {
        minecraftVersion("1.21.3")
        runDirectory(rootProject.projectDir.resolve("run"))
        val file = rootProject.projectDir.resolve("run/server.jar")
        if (file.exists()) serverJar(file)

        if (name == "runServer") {
            downloadPlugins {
                url("https://download.luckperms.net/1567/bukkit/loader/LuckPerms-Bukkit-5.4.150.jar")
                url("https://repo.extendedclip.com/content/repositories/placeholderapi/me/clip/placeholderapi/2.11.6/placeholderapi-2.11.6.jar")
            }
        }
    }
}
