plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
    alias(libs.plugins.jetbrainsCompose)
    id("buildsrc.convention.kotlin-jvm")

    // Apply the Application plugin to add support for building an executable JVM application.
    application
}

dependencies {
    // Project "app" depends on project "lib". (Project paths are separated with ":", so ":lib" refers to the top-level "lib" project.)
    implementation(project(":lib"))
    implementation(libs.bundles.kotlinxEcosystem)
    implementation(libs.bundles.kotlinLibGDX)
    implementation("com.badlogicgames.gdx:gdx-platform:1.10.0:natives-desktop")
    implementation(compose.desktop.currentOs)
    implementation(compose.material3)
    implementation(compose.materialIconsExtended)
}

application {
    // Define the Fully Qualified Name for the application main class
    // (Note that Kotlin compiles `App.kt` to a class with FQN `com.example.app.AppKt`.)
    mainClass = "org.example.app.AppKt"
}
