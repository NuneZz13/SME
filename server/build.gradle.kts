import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.spring)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.springBoot)
    alias(libs.plugins.springDependencyManagement)
    war
}

java.sourceCompatibility = JavaVersion.VERSION_1_8

dependencies {
    implementation(project(":common"))
    implementation(libs.kform.core)
    implementation(libs.kotlinxSerialization.json)
    implementation(libs.kotlinxCoroutines.core.jvm)
    implementation(libs.kotlinxCoroutines.reactor)

    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    implementation(kotlin("reflect"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    providedRuntime("org.springframework.boot:spring-boot-starter-tomcat")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks {
    withType<KotlinCompile> { kotlinOptions { freeCompilerArgs = listOf("-Xjsr305=strict") } }

    withType<Test> { useJUnitPlatform() }

    /** Build the client and copy the build output into the server as static resources. */
    val copyClientResources by
        registering(Copy::class) {
            from(getByPath(":client:npmRunBuild"))
            into("build/resources/main/static")
        }
    processResources { dependsOn(copyClientResources) }
}
