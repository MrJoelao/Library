plugins {
    application
}

application {
    mainClass.set("com.library.client.ClientApplication")
}

dependencies {
    implementation(project(":common"))
}
