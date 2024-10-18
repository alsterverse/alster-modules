import java.util.Properties

val githubProperties = Properties().apply {
    load(rootProject.file("github.properties").inputStream())
}

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("com.vanniktech.maven.publish")
}

val versionName = githubProperties.getProperty("version")

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "17"
            }
        }
        publishLibraryVariants("release", "debug")
    }


    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach {
        it.compilations.getByName("main") {
            val nskeyvalueobserving by cinterops.creating {
                defFile("src/nativeInterop/cinterop/nskeyvalueobserving.def")
            }
        }

    }

    jvmToolchain(17)

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.media3.exoplayer)
            implementation(libs.androidx.media3.exoplayer.hls)
            implementation(libs.androidx.media3.exoplayer.dash)
        }
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
        }
        iosMain.dependencies {
        }
    }
}

android {
    namespace = "se.alster.player"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
}


publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Alsterverse/alster-modules")
            credentials {
                username = githubProperties.getProperty("gpr.user")
                password = githubProperties.getProperty("gpr.token")
            }
        }
    }
}

mavenPublishing {
    coordinates("se.alster", "player", versionName)
}