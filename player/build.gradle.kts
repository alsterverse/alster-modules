import java.util.Properties

val githubProperties = Properties().apply {
    load(rootProject.file("github.properties").inputStream())
}

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    id("maven-publish")
}

val versionName = "1.0.3"

group = "se.alster"
version = versionName

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
    publications {
        create<MavenPublication>("gpr") {
            from(components["kotlin"])
            groupId = "se.alster"
            artifactId = "player"
            version = versionName
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
