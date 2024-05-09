@file:Suppress("UnstableApiUsage")

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        mavenLocal()
        maven("https://papermc.io/repo/repository/maven-public/") { name = "PaperMC" }
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/") { name = "PlaceholderAPI" }
        maven("https://repo.activmine.ru/public") { name = "ActivMine" }
    }
}

rootProject.name = "Heads"

include("api")
include("plugin")
