import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.freefair.lombok") version "6.3.0"
}

group = "com.github.rephrasing"
version = "0.0.1"

repositories {
    mavenCentral()
    maven {
        url = uri("https://papermc.io/repo/repository/maven-public/")
    }
    maven {
        url =  uri("https://jitpack.io")
    }
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
    compileOnly("com.github.Rephrasing:Stardust:0.0.4")
    implementation("com.github.Rephrasing:GalaxyAPI:1.1")
}
tasks.named<ShadowJar>("shadowJar").configure {

    minimize()
    archiveFileName.set("${project.name}-v${project.version}.jar")
    destinationDirectory.set(file("$rootDir/output"))
}

tasks {
    build {
        dependsOn(shadowJar)
    }
}