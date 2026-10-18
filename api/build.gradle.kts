plugins {
    id("maven-publish")
}

val repositoryProvider: Provider<String> = providers.gradleProperty("projectVersion").map { currentVersion ->
    if (currentVersion.endsWith("-SNAPSHOT")) "snapshots" else "releases"
}

publishing {
    repositories {
        maven("https://repo.rafaelkauwu.me/${repositoryProvider.get()}/") {
            name = "rafaelkauwu"
            credentials(PasswordCredentials::class)
        }
    }

    publications {
        register<MavenPublication>("maven") {
            artifactId = "heads-api"
            from(components["java"])
        }
    }
}
