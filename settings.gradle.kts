@file:Suppress("UnstableApiUsage")

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.PREFER_SETTINGS)
    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
        maven("https://repo.songoda.com/repository/public/")
        maven("http://repo.denaryworld.ru/snapshots/") {
            isAllowInsecureProtocol = true
        }
    }

    versionCatalogs {
        create("libs") {
            version("crystal", "2.0.0-SNAPSHOT")

            library("bukkit", "io.sapphiremc.sapphire:sapphire-api:1.19.3-R0.1-SNAPSHOT")
            library("papi", "me.clip:placeholderapi:2.11.2")

            library("lombok", "org.projectlombok:lombok:1.18.24")

            library("crystal-config", "me.denarydev.crystal.shared", "config").versionRef("crystal")
            library("crystal-locale", "me.denarydev.crystal.shared", "locale").versionRef("crystal")
            library("crystal-gui", "me.denarydev.crystal.bukkit", "gui").versionRef("crystal")
            library("crystal-utils", "me.denarydev.crystal.bukkit", "utils").versionRef("crystal")
            library("crystal-serializers", "me.denarydev.crystal.bukkit", "serializers").versionRef("crystal")

            bundle("crystal", listOf("crystal-config", "crystal-locale", "crystal-gui", "crystal-utils", "crystal-serializers"))
        }
    }
}

rootProject.name = "SkyHeads"

include("api")
include("plugin")
