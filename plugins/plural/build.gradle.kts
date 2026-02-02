plugins {
    java
    id("com.gradleup.shadow")
}

group = "win.doughmination.plural"
version = "1.0.0"
description = "Plural"

repositories {
    mavenCentral()
}

dependencies {
    // Hytale API dependency - compileOnly since it's provided by the server
    compileOnly(files("../../libs/HytaleServer.jar"))

    // GSON - shaded into the plugin
    implementation("com.google.code.gson:gson:2.13.2")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

// Set duplicate strategy for all Copy tasks
tasks.withType<Copy> {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
}

tasks {
    shadowJar {
        archiveBaseName.set("Plural")
        archiveClassifier.set("")

        // Relocate GSON to avoid conflicts with other plugins
        relocate("com.google.gson", "win.doughmination.plural.libs.gson")

        // Only include runtime dependencies (not compileOnly)
        configurations = listOf(project.configurations.runtimeClasspath.get())
    }

    build {
        dependsOn(shadowJar)
    }
}