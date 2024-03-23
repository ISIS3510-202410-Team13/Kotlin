buildscript {
    dependencies {
        classpath(libs.google.services)
    }
}
plugins {
    alias(libs.plugins.gradle.versions)
    alias(libs.plugins.version.catalog.update)
    id("com.google.gms.google-services") version "4.4.1" apply false
//    alias(libs.plugins.kotlin.android) apply false
}

apply("${project.rootDir}/buildscripts/toml-updater-config.gradle")
