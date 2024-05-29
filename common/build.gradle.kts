import com.github.gradle.node.npm.task.NpmTask
import org.jetbrains.kotlin.gradle.dsl.KotlinJsCompile

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.nodeGradle)
}

kotlin {
    jvm {
        jvmToolchain(8)
        testRuns["test"].executionTask.configure { useJUnitPlatform() }
    }
    js {
        binaries.library()
        browser { testTask { useMocha() } }
        useEsModules()
        generateTypeScriptDefinitions()
    }

    sourceSets {
        all {
            // Opt in to the experimental `JsExport` annotation
            languageSettings.optIn("kotlin.js.ExperimentalJsExport")
        }

        commonMain.dependencies {
            api(libs.kform.core)
//                api(libs.kform.core.jvm) // bug IntelliJ -> https://jira.opensoft.pt/browse/LF-786
            api(libs.kotlinxCoroutines.core)
            api(libs.kotlinxDatetime)
            api(libs.kotlinxSerialization.json)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotlinxCoroutines.test)
        }
        jvmMain
        jvmTest
        jsMain.dependencies {
            implementation(libs.kform.jsBindings)
        }
        jsTest
    }
}

tasks {
    // Enable ES6 classes generation
    withType<KotlinJsCompile>().configureEach { kotlinOptions { useEsClasses = true } }

    // Packs the result of the JS compilation, for consumption by the client
    val packJsPackage by
        registering(NpmTask::class) {
            group = "build"
            dependsOn("jsBrowserProductionLibraryDistribution")

            val libraryDir = file("build/dist/js/productionLibrary")
            val packagesDir = mkdir("build/packages")
            npmCommand = listOf("pack", "$libraryDir", "--pack-destination", "$packagesDir")

            inputs.dir(libraryDir)
            outputs.dir(packagesDir)
        }
}
