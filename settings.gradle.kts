pluginManagement {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        maven("https://maven.myket.ir")

        // Mintegral ads
        maven {
            url =
                uri("https://dl-maven-android.mintegral.com/repository/mbridge_android_sdk_oversea")
            content { includeGroupByRegex("com.mbridge.*") }
        }

        // ChartBoost ads
        maven {
            url = uri("https://cboost.jfrog.io/artifactory/chartboost-mediation")
            content { includeGroup("com.chartboost") }
        }
        maven {
            url = uri("https://cboost.jfrog.io/artifactory/chartboost-ads")
            content { includeGroup("com.chartboost") }
        }
        maven {
            url = uri("https://cboost.jfrog.io/artifactory/chartboost-core")
            content { includeGroup("com.chartboost") }
        }

        // Wortise ads
        maven {
            url = uri("https://maven.wortise.com/artifactory/public")
            content { includeGroupByRegex("com.wortise.*") }
        }

        maven("https://storage.googleapis.com/download.flutter.io")
    }
}

rootProject.name = "MediationAndroidSample"

includeBuild("build-logic")

include(":sample-kotlin")
include(":sample-java")
include(":sample-jetpack-compose")
include(":shared")
