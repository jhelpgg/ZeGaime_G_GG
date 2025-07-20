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
    api(project(":animations"))
    api(project(":resources"))
    api(project(":preferences"))

    implementation(files("libs/lwjgl/lwjgl.jar"))
    runtimeOnly(files("libs/lwjgl/lwjgl-natives-linux.jar"))
    runtimeOnly(files("libs/lwjgl/lwjgl-natives-linux-arm32.jar"))
    runtimeOnly(files("libs/lwjgl/lwjgl-natives-linux-arm64.jar"))
    runtimeOnly(files("libs/lwjgl/lwjgl-natives-macos.jar"))
    runtimeOnly(files("libs/lwjgl/lwjgl-natives-windows.jar"))
    runtimeOnly(files("libs/lwjgl/lwjgl-natives-windows-x86.jar"))

    implementation(files("libs/lwjgl-glfw/lwjgl-glfw.jar"))
    runtimeOnly(files("libs/lwjgl-glfw/lwjgl-glfw-natives-linux.jar"))
    runtimeOnly(files("libs/lwjgl-glfw/lwjgl-glfw-natives-linux-arm32.jar"))
    runtimeOnly(files("libs/lwjgl-glfw/lwjgl-glfw-natives-linux-arm64.jar"))
    runtimeOnly(files("libs/lwjgl-glfw/lwjgl-glfw-natives-macos.jar"))
    runtimeOnly(files("libs/lwjgl-glfw/lwjgl-glfw-natives-windows.jar"))
    runtimeOnly(files("libs/lwjgl-glfw/lwjgl-glfw-natives-windows-x86.jar"))

    implementation(files("libs/lwjgl-jawt/lwjgl-jawt.jar"))

    implementation(files("libs/lwjgl-opengl/lwjgl-opengl.jar"))
    runtimeOnly(files("libs/lwjgl-opengl/lwjgl-opengl-natives-linux.jar"))
    runtimeOnly(files("libs/lwjgl-opengl/lwjgl-opengl-natives-linux-arm32.jar"))
    runtimeOnly(files("libs/lwjgl-opengl/lwjgl-opengl-natives-linux-arm64.jar"))
    runtimeOnly(files("libs/lwjgl-opengl/lwjgl-opengl-natives-macos.jar"))
    runtimeOnly(files("libs/lwjgl-opengl/lwjgl-opengl-natives-windows.jar"))
    runtimeOnly(files("libs/lwjgl-opengl/lwjgl-opengl-natives-windows-x86.jar"))

    implementation(files("libs/lwjgl-openal/lwjgl-openal.jar"))
    runtimeOnly(files("libs/lwjgl-openal/lwjgl-openal-natives-linux.jar"))
    runtimeOnly(files("libs/lwjgl-openal/lwjgl-openal-natives-linux-arm32.jar"))
    runtimeOnly(files("libs/lwjgl-openal/lwjgl-openal-natives-linux-arm64.jar"))
    runtimeOnly(files("libs/lwjgl-openal/lwjgl-openal-natives-macos.jar"))
    runtimeOnly(files("libs/lwjgl-openal/lwjgl-openal-natives-windows.jar"))
    runtimeOnly(files("libs/lwjgl-openal/lwjgl-openal-natives-windows-x86.jar"))

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}