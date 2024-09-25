import java.util.Properties

val githubProperties = Properties().apply {
    load(rootProject.file("github.properties").inputStream())
}

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    `maven-publish`
}

group = "se.alster"
version = "1.0.1"


kotlin {
    androidTarget {
        publishLibraryVariants("release", "debug")
    }


    iosX64()
    iosArm64()
    iosSimulatorArm64()

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
    publications {
        create<MavenPublication>("gpr") {
            from(components["kotlin"])
        }
    }
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/Mats-Hjalmar/alster-modules")
            credentials {
                username = githubProperties.getProperty("gpr.user")
                password = githubProperties.getProperty("gpr.token")
            }
        }
    }
}
