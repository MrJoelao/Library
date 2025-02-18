allprojects {
    repositories {
        mavenCentral()
    }
}

subprojects {
    version = "1.0-SNAPSHOT"

    if (project.name == "rmi-server") {
        apply(plugin = "java")
        apply(plugin = "application")

        configure<JavaPluginExtension> {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }

        dependencies {
            "testImplementation"("org.junit.jupiter:junit-jupiter:5.9.2")
        }

        tasks.named<Test>("test") {
            useJUnitPlatform()
        }
    }

    if (project.name == "web-client") {
        apply(plugin = "java")
        apply(plugin = "org.springframework.boot")
        apply(plugin = "io.spring.dependency-management")

        configure<JavaPluginExtension> {
            sourceCompatibility = JavaVersion.VERSION_11
            targetCompatibility = JavaVersion.VERSION_11
        }

        dependencies {
            "implementation"("org.springframework.boot:spring-boot-starter-web")
            "implementation"("org.springframework.boot:spring-boot-starter-thymeleaf")
            "testImplementation"("org.springframework.boot:spring-boot-starter-test")
        }

        tasks.named<Test>("test") {
            useJUnitPlatform()
        }
    }
}

plugins {
    java
    id("org.springframework.boot") version "2.7.8" apply false
    id("io.spring.dependency-management") version "1.0.15.RELEASE" apply false
}
