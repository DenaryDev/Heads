@file:Suppress("UnstableApiUsage")

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            version("crystal", "2.0.3")

            library("bukkit", "io.sapphiremc.sapphire:sapphire-api:1.20.2-R0.1-SNAPSHOT")
            library("papi", "me.clip:placeholderapi:2.11.5")

            library("lombok", "org.projectlombok:lombok:1.18.26")

            library("crystal-config", "me.denarydev.crystal.shared", "config").versionRef("crystal")
            library("crystal-gui", "me.denarydev.crystal.paper", "gui").versionRef("crystal")
            library("crystal-utils", "me.denarydev.crystal.paper", "utils").versionRef("crystal")
            library("crystal-serializers", "me.denarydev.crystal.paper", "serializers").versionRef("crystal")

            bundle(
                "crystal",
                listOf("crystal-config", "crystal-gui", "crystal-utils", "crystal-serializers")
            )
        }
    }
}

rootProject.name = "Heads"

include("api")
include("plugin")
