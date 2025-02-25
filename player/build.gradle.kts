import java.util.Properties

val githubProperties = Properties().apply {
    load(rootProject.file("github.properties").inputStream())
}

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.composeMultiplatform)

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
        publishLibraryVariants("release")
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
            implementation(libs.androidx.media3.ui)
        }
        commonMain.dependencies {
            implementation(libs.kotlinx.coroutines.core)
            implementation(compose.foundation)
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