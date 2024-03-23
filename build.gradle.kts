
plugins {
    alias(libs.plugins.gradle.versions)
    alias(libs.plugins.version.catalog.update)
    id("com.google.gms.google-services") version "4.4.1" apply false
}

apply("${project.rootDir}/buildscripts/toml-updater-config.gradle")
