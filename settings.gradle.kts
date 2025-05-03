pluginManagement {
    repositories {
        google()
        mavenLocal()
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

        // IronSource ads
        maven {
            url = uri("https://android-sdk.is.com")
            content { includeGroupByRegex("com.ironsource.*") }
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

        // Wortise ads
        maven {
            url = uri("https://maven.wortise.com/artifactory/public")
            content { includeGroupByRegex("com.wortise.*") }
        }
        maven {
            url = uri("https://artifact.bytedance.com/repository/pangle")
            content { includeGroupByRegex("com.pangle.*") }
        }
    }
}

rootProject.name = "TapsellSample"
include(":sample-kotlin")
include(":sample-java")
include(":sample-jetpack-compose")
include(":shared")
