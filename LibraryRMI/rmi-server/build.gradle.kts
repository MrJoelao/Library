plugins {
    java
    application
}

group = "com.library.rmi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

application {
    mainClass.set("com.library.rmi.server.LibraryServer")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.withType<JavaExec> {
    jvmArgs = listOf(
        "-Djava.rmi.server.hostname=localhost",
        "-Djava.rmi.server.useCodebaseOnly=false"
    )
}
