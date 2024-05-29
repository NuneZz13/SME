plugins {
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

allprojects {
    repositories {
        maven {
            url = uri("https://nexus.opensoft.pt/content/groups/maven-all/")
        }
        mavenCentral()
    }
}
