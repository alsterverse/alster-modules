# Alster Modules

## Overview

Alster Player is a Kotlin Multiplatform project designed to provide a media player that works across Android and iOS platforms. The project leverages Kotlin Multiplatform Mobile (KMM) to share code between Android and iOS, while also using platform-specific APIs for media playback.

## Project Structure

The project is structured as follows:

- `commonMain`: Shared code between Android and iOS.
- `androidMain`: Android-specific code.
- `iosMain`: iOS-specific code.

## Requirements

- Android Studio Koala | 2024.1.1
- Kotlin 1.8.0 or higher
- Gradle 7.0 or higher
- Xcode 13.0 or higher (for iOS development)

## Setup

### Clone the Repository

```sh
git clone https://github.com/Alsterverse/alster-modules.git
cd alster-modules
```

### Configure GitHub Packages

Create a `github.properties` file in the root directory with the following content:

```
gpr.user=YOUR_GITHUB_USERNAME
gpr.token=YOUR_GITHUB_TOKEN
```

### Open in Android Studio

1. Open Android Studio.
2. Select "Open an existing project".
3. Navigate to the cloned repository and open it.

## Usage

### PlayerControllerIOS

The `PlayerControllerIOS` class is responsible for managing the AVPlayer instance on iOS. It includes methods for loading, playing, pausing, and seeking within a video.

### PlayerControllerAndroid

The `PlayerControllerAndroid` class is responsible for managing the ExoPlayer instance on Android. It includes similar methods for loading, playing, pausing, and seeking within a video.

## Dependencies

### Common

- `kotlinx.coroutines.core`

### Android

- `androidx.media3.exoplayer`
- `androidx.media3.exoplayer.hls`
- `androidx.media3.exoplayer.dash`

### iOS

- `AVFoundation`

## Publishing

The project is configured to publish to GitHub Packages. To publish a new version, run:

```sh
./gradlew publish
```

## Installation

To install a module from `Alster Modules`, follow these steps:

1. **Configure the `repositories` block in your `build.gradle.kts` file:**

   ```kotlin
   repositories {
       maven {
           url = uri("https://maven.pkg.github.com/Alsterverse/alster-modules")
       }
       mavenCentral()
       google()
   }
   ```

2. **Add the dependency to the commonMain source set:**

   ```kotlin
   commonMain.dependencies {
       implementation("se.alster:player:<version>")
   }
   ```

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.

## Contributing

Contributions are welcome! Please open an issue or submit a pull request.

## Contact

For any inquiries, please contact hello@alster.se.