plugins {
    id("org.springframework.boot") version "2.7.8" apply false
    id("io.spring.dependency-management") version "1.0.15.RELEASE" apply false
    java
}

allprojects {
    group = "com.library"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")

    if (project.name != "common") {
        apply(plugin = "org.springframework.boot")
    }

    configure<JavaPluginExtension> {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    dependencies {
        if (project.name != "common") {
            implementation("org.springframework.boot:spring-boot-starter-web")
        }
        testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
        testImplementation("org.springframework.boot:spring-boot-starter-test")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}
