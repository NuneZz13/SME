import com.github.gradle.node.npm.task.NpmTask

plugins {
    alias(libs.plugins.nodeGradle)
    base
}

tasks {
    val commonPackage =
        project(":common").file("build/packages/SMEFrontEndKform-common-$version.tgz")

    val npmInstallCommon by
        registering(NpmTask::class) {
            group = "build"
            dependsOn(":common:packJsPackage")
            npmCommand = listOf("install", "SMEFrontEndKform-common@file:$commonPackage")

            inputs.file(commonPackage)
            outputs.dir("node_modules/SMEFrontEndKform-common")
        }
    npmInstall { dependsOn(npmInstallCommon) }

    val npmRunDev by
        registering(NpmTask::class) {
            group = "development"
            dependsOn(npmInstall)
            npmCommand = listOf("run", "dev")
            environment = mapOf("VITE_APP_VERSION" to "$version") // version defined in gradle.properties
        }

    val npmRunBuild by
        registering(NpmTask::class) {
            group = "build"
            dependsOn(npmInstall)
            npmCommand = listOf("run", "build")
            environment = mapOf("VITE_APP_VERSION" to "$version")

            inputs.dir("public")
            inputs.dir("src")
            inputs.file(commonPackage)
            inputs.file("index.html")
            inputs.file("package.json")
            inputs.file("vite.config.ts")
            inputs.file("tsconfig.json")
            outputs.dir("build/dist")
        }

    val npmRunLint by
        registering(NpmTask::class) {
            group = "verification"
            dependsOn(npmInstall)
            npmCommand = listOf("run", "lint")

            inputs.dir("src")
            inputs.file(".eslintrc.json")
            inputs.file("package.json")
            outputs.upToDateWhen { true }
        }

    assemble { dependsOn(npmRunBuild) }
    check { dependsOn(npmRunLint) }
    clean { delete(npmInstallCommon) }
}
