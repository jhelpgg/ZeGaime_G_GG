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
    api(project(":images"))
    api(project(":sounds"))
    api(project(":video"))
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}