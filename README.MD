# Alster Modules

## Overview

Alster Player is a Kotlin Multiplatform project designed to provide a media player that works across Android and iOS platforms. The project leverages Kotlin Multiplatform Mobile (KMM) to share code between Android and iOS, while also using platform-specific APIs for media playback.

## Installation

To install a module from `Alster Modules`, follow these steps:

1. **Configure the `repositories` block in your `settings.gradle.kts` file:**

   ```kotlin
   repositories {
       maven {
           url = uri("https://maven.pkg.github.com/Alsterverse/alster-modules")
       }
   }
   ```

2. **Add the dependency to the `commonMain` source set:**

   ```kotlin
   commonMain.dependencies {
       implementation("se.alster:player:<version>")
       implementation("se.alster:ui-state:<version>")
   }
   ```

## Publishing

The project is configured to publish to GitHub Packages. To publish a new version, simply trigger a release in GitHub, and an action will create a new release.

## Creating New Modules

To create more modules, you can copy and paste an already existing module (e.g., `ui-state`) and include it in the project from `settings.gradle.kts`.

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.

## Contributing

Contributions are welcome! Please open an issue or submit a pull request.

## Contact

For any inquiries, please contact hello@alster.se.