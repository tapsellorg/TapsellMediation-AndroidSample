plugins {
    id("tapsell-application")
}

android {
    namespace = "ir.tapsell.sample"

    defaultConfig {
        applicationId = "ir.tapsell.sample"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        multiDexEnabled = true

        addManifestPlaceholders(
            mapOf(
                "TapsellMediationAppKey" to "76798342-99a7-4a5f-bf5a-60a088d5dcfb",
                "TapsellMediationAdmobAdapterSignature" to "ca-app-pub-3940256099942544~3347511713",
            )
        )
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    splits {
        abi {
            isEnable = true
            isUniversalApk = true
            reset()
            include("x86", "x86_64", "armeabi-v7a", "arm64-v8a")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    lint {
        checkReleaseBuilds = false
        abortOnError = false
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.google.appset)
    implementation(libs.google.ads.identifier)
    implementation(project(":shared"))

    implementation(libs.tapsell)
    implementation(libs.adapter.legacy)
    implementation(libs.adapter.legacy.ima.extension)
    implementation(libs.adapter.admob)
    implementation(libs.adapter.unityads)
    implementation(libs.adapter.fyber)
    implementation(libs.adapter.applovin)
    implementation(libs.adapter.ironsource)
    implementation(libs.adapter.liftoff)
    implementation(libs.adapter.mintegral)
    // disable networks that uses incompatible kotlin versions until upcoming releases
    // implementation(libs.adapter.yandex)
    // implementation(libs.adapter.chartboost)
    // implementation(libs.adapter.wortise)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
