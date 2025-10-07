plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly(gradleApi())
    implementation(libs.androidGradlePlugin)
}

gradlePlugin {
    plugins {
        register("tapsell-application") {
            id = "tapsell-application"
            implementationClass = "TapsellAppPlugin"
        }
    }
}