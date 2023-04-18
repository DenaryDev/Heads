@file:Suppress("UnstableApiUsage")

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
        maven("https://repo.songoda.com/repository/public/")
        maven("https://the-planet.fun/repo/snapshots/")
    }

    versionCatalogs {
        create("libs") {
            version("crystal", "2.0.0")

            library("bukkit", "io.sapphiremc.sapphire:sapphire-api:1.19.4-R0.1-SNAPSHOT")
            library("papi", "me.clip:placeholderapi:2.11.3")

            library("lombok", "org.projectlombok:lombok:1.18.26")

            library("crystal-config", "me.denarydev.crystal.shared", "config").versionRef("crystal")
            library("crystal-locale", "me.denarydev.crystal.shared", "locale").versionRef("crystal")
            library("crystal-gui", "me.denarydev.crystal.bukkit", "gui").versionRef("crystal")
            library("crystal-utils", "me.denarydev.crystal.bukkit", "utils").versionRef("crystal")
            library("crystal-serializers", "me.denarydev.crystal.bukkit", "serializers").versionRef("crystal")

            bundle(
                "crystal",
                listOf("crystal-config", "crystal-locale", "crystal-gui", "crystal-utils", "crystal-serializers")
            )
        }
    }
}

rootProject.name = "SkyHeads"

include("api")
include("plugin")
