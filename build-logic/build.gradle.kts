plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
    mavenCentral()
    google()
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
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