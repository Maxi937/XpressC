import org.jetbrains.kotlin.gradle.plugin.mpp.pm20.util.distsDirectory
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "org.example"
version = "1.0-SNAPSHOT"


plugins {
    kotlin("jvm") version "1.9.22"
    id("org.beryx.runtime") version "1.8.4"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("org.json:json:20231013")
    implementation("com.gitlab.mvysny.konsume-xml:konsume-xml:1.1")
    implementation("com.github.doyaaaaaken:kotlin-csv-jvm:1.9.3")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("MainKt")
}

runtime {
    imageDir.set(project.file("${project.distsDirectory.get()}/xpression-bdt"))
    imageZip.set(project.file("${project.distsDirectory}/xpression-bdt/xpression-bdt.zip"))
    options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
    modules.set(listOf("java.desktop", "jdk.unsupported", "java.scripting", "java.logging", "java.xml"))
}