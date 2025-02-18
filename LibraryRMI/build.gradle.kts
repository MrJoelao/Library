plugins {
    java
    application
}

group = "library"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
}

tasks.compileJava {
    options.release.set(11)
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("server.LibraryServer")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
    modularity.inferModulePath.set(true)
}

tasks.register<JavaExec>("runServer") {
    group = "application"
    mainClass.set("server.LibraryServer")
    classpath = sourceSets["main"].runtimeClasspath
    jvmArgs = listOf(
        "-Djava.security.policy=security.policy",
        "-Djava.rmi.server.codebase=file:${projectDir}/build/classes/java/main/"
    )
}

tasks.register<JavaExec>("runClient") {
    group = "application"
    mainClass.set("client.LibraryClient")
    classpath = sourceSets["main"].runtimeClasspath
    jvmArgs = listOf(
        "-Djava.security.policy=security.policy"
    )
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "server.LibraryServer"
    }
}
