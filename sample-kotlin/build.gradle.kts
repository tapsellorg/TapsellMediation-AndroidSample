import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("tapsell-application")
    alias(libs.plugins.kotlinAndroid)
}

android {
    namespace = "ir.tapsell.sample"

    setProperty("archivesBaseName", properties["TAPSELL_APP_NAME"] as String)

    defaultConfig {
        applicationId = "ir.tapsell.sample"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        multiDexEnabled = true

        addManifestPlaceholders(
            mapOf(
                "TapsellMediationAppKey" to properties["TAPSELL_APP_ID"] as String,
                "TapsellMediationAdmobAdapterSignature" to properties["ADMOB_APP_ID"] as String,
            )
        )
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
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
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_1_8)
        }
    }
    lint {
        checkReleaseBuilds = false
        abortOnError = false
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.activity.ktx)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.kotlin.reflect)
    implementation(libs.coil)
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
    implementation(libs.adapter.yandex)
    implementation(libs.adapter.chartboost)
    implementation(libs.adapter.wortise)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    debugImplementation(libs.leakcanary)
}

