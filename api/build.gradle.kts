publishing {
    publications.create<MavenPublication>("maven") {
        artifactId = "heads-${project.name}"
        from(components["java"])
    }
}