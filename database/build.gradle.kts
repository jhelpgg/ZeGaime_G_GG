plugins {
    kotlin("jvm")
}

group = "fr.khelp.zegaime"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    api(project(":utils"))
    implementation(files("libs/hsqldb.jar"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}