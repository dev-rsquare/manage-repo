import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    application
    idea
    maven
    kotlin("jvm") version "1.3.41"

    val kotlinVersion = "1.3.31"
    id("org.jetbrains.kotlin.plugin.jpa") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.spring") version kotlinVersion
    id("org.springframework.boot") version "2.1.4.RELEASE"
}

apply(plugin = "kotlin-jpa")
apply(plugin = "kotlin-allopen")
apply(plugin = "io.spring.dependency-management")


group = "com.github.dev-rsquare"
version = "1.0.0"

repositories {
    mavenCentral()
    maven {
        setUrl("https://jitpack.io")
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("com.fasterxml.jackson.core:jackson-core:2.9.9")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.9.9")
    implementation("javax.persistence:javax.persistence-api:2.2")
    implementation("com.github.jitpack:gradle-simple:1.0.5")

    // spring
    implementation("org.springframework.boot:spring-boot-starter-cache")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.data:spring-data-redis")
}

application {
    applicationName = "manage-entity"
    group = "com.github.dev-rsquare"
    mainClassName = "kr.co.rsqure.lib.AppMain"
}

tasks {
    withType<KotlinCompile> {
        kotlinOptions.jvmTarget = "1.8"
    }

    withType<Jar>{
        manifest {
            attributes(mapOf("Main-Class" to application.mainClassName))
        }
        val version = "19.09.1-SNAPSHOT"
        archiveName = "${application.applicationName}-$version.jar"
        from(
            configurations.compileClasspath.get().map {
                if (it.isDirectory) it else zipTree(it)
            }
       )
    }

    install {
        repositories.withGroovyBuilder {
            "mavenInstaller" {
                "pom" {
                    setProperty("version", "19.09.10")
                    setProperty("artifactId", "manage-entity")
                }
            }
        }
    }

    test {
        useJUnit()
    }
}
