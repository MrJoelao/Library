plugins {
    application
}

application {
    mainClass.set("com.library.server.ServerApplication")
}

dependencies {
    implementation(project(":common"))
}
