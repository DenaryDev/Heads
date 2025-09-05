plugins {
    id("maven-publish")
}

publishing {
    repositories {
        val repo = if (rootProject.version.toString().endsWith("-SNAPSHOT")) "snapshots" else "releases"
        maven("https://repo.prostocraft.ru/$repo/") {
            name = "prostocraft"
            credentials(PasswordCredentials::class)
        }
    }

    publications.create<MavenPublication>("maven") {
        artifactId = "heads-api"
        from(components["java"])
    }
}
