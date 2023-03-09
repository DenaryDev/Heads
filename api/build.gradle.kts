dependencies {
    compileOnly(libs.bukkit)
    compileOnly(libs.lombok)

    annotationProcessor(libs.lombok)
}

publishing {
    publications.create<MavenPublication>("maven") {
        artifactId = "heads-api"
        from(components["java"])
    }
}