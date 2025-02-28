plugins {
    `java-library`
    id("io.spring.dependency-management")
}

dependencies {
    api("org.springframework:spring-context:5.3.29")
    implementation("org.springframework.boot:spring-boot-starter:2.7.8")
    testImplementation("org.springframework.boot:spring-boot-starter-test:2.7.8")
}
