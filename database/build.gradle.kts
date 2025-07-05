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
    implementation(project(":security"))
    implementation(files("libs/hsqldb.jar"))
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.0")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}