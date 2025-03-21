plugins {
    java
    application
    id("org.springframework.boot") version "2.7.8"
    id("io.spring.dependency-management") version  "1.0.15.RELEASE"
}

group = "com.library.rmi"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-autoconfigure")
    implementation("org.springframework:spring-context")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.2")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

application {
    mainClass.set("com.library.rmi.server.web.ServerWebApplication")
}

springBoot {
    mainClass.set("com.library.rmi.server.web.ServerWebApplication")
}

tasks.getByName<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
    mainClass.set("com.library.rmi.server.web.ServerWebApplication")
    jvmArgs = listOf(
        "-Djava.rmi.server.hostname=localhost",
        "-Djava.rmi.server.useCodebaseOnly=false"
    )
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
