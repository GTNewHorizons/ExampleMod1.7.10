# Example Forge Mod for Minecraft 1.7.10

[![](https://jitpack.io/v/GTNewHorizons/ExampleMod1.7.10.svg)](https://jitpack.io/#GTNewHorizons/ExampleMod1.7.10)
[![](https://github.com/GTNewHorizons/ExampleMod1.7.10/actions/workflows/build-and-test.yml/badge.svg)](https://github.com/GTNewHorizons/ExampleMod1.7.10/actions/workflows/build-and-test.yml)

An example mod for Minecraft 1.7.10 with Forge focussed on a stable, updatable setup.

### Motivation

We had our fair share in struggles with build scripts for Minecraft Forge. There are quite a few pitfalls from non-obvious error messages. This Example Project provides you a build system you can adapt to over 90% of Minecraft Forge mods and can easily be updated if need be.

### Help! I'm stuck!

We all have been there! Check out our [FAQ](https://github.com/GTNewHorizons/ExampleMod1.7.10/blob/main/docs/FAQ.md). If that doesn't help, please open an issue.

### Getting started

Creating mod from scratch:
1. Unzip [project starter](https://github.com/GTNewHorizons/ExampleMod1.7.10/releases/download/master-packages/starter.zip) into project directory.
2. Replace placeholders in LICENSE-template and rename it to LICENSE, or remove LICENSE-template and put any other license you like on your code. This is an permissive OSS project and we encourage you participate in OSS movement by having permissive license like one in template. You can find out pros and cons of OSS software in [this article](https://www.freecodecamp.org/news/what-is-great-about-developing-open-source-and-what-is-not/)
3. Ensure your project is under VCS. For example initialise git repository by running `git init; git commit --message "initialized repository"`.
4. Replace placeholders (edit values in gradle.properties, change example package and class names, etc.)
5. Run `./gradlew build`
6. Make sure to check out the rest sections of this file.
7. You are good to go!

We also have described guidelines for existing mod [migration](docs/migration.md) and [porting](docs/porting.md)

### Features

 - Updatable: Replace [`build.gradle`](https://github.com/GTNewHorizons/ExampleMod1.7.10/blob/main/build.gradle) with a newer version
 - Optional API artifact (.jar)
 - Optional version replacement in Java files
 - Optional shadowing of dependencies
 - Simplified setup of Mixin and example
 - Scala support (add sources under `src/main/scala/` instead of `src/main/java/`)
 - Optional named developer account for consistent player progression during testing
 - Boilerplate forge mod as starting point
 - Improved warnings for pitfalls
 - Git Tags integration for versioning
 - [Jitpack](https://jitpack.io) CI
 - GitHub CI:
   - Releasing your artifacts on new tags pushed. Push git tag named after version (e.g. 1.0.0) which will trigger a release of artifacts with according names.
   - Running smoke test for server startup. On any server crash occurring workflow will fail and print the crash log.

### Files
 - [`build.gradle`](https://github.com/GTNewHorizons/ExampleMod1.7.10/blob/main/build.gradle): This is the core script of the build process. You should not need to tamper with it, unless you are trying to accomplish something out of the ordinary. __Do not touch this file! You will make a future update near impossible if you do so!__
 - [`gradle.properties`](https://github.com/GTNewHorizons/ExampleMod1.7.10/blob/main/gradle.properties): The core configuration file. It includes
 - [`dependencies.gradle[.kts]`](https://github.com/GTNewHorizons/ExampleMod1.7.10/blob/main/dependencies.gradle): Add your mod's dependencies in this file. This is separate from the main build script, so you may replace the [`build.gradle`](https://github.com/SinTh0r4s/ExampleMod1.7.10/blob/main/build.gradle) if an update is available.
 - [`repositories.gradle[.kts]`](https://github.com/GTNewHorizons/ExampleMod1.7.10/blob/main/repositories.gradle): Add your dependencies' repositories. This is separate from the main build script, so you may replace the [`build.gradle`](https://github.com/SinTh0r4s/ExampleMod1.7.10/blob/main/build.gradle) if an update is available.
 - [`jitpack.yml`](https://github.com/GTNewHorizons/ExampleMod1.7.10/blob/main/jitpack.yml): Ensures that your mod is available as import over [Jitpack](https://jitpack.io).
 - [`.github/workflows/gradle.yml`](https://github.com/GTNewHorizons/ExampleMod1.7.10/blob/main/.github/workflows/gradle.yml): A simple CI script that will build your mod any time it is pushed to `master` or `main` and publish the result as release in your repository. This feature is free with GitHub if your repository is public.

### Forge's Access Transformers

You may activate Forge's Access Transformers by defining a configuration file in `gradle.properties`.

Check out the [`example-access-transformers`](https://github.com/GTNewHorizons/ExampleMod1.7.10/tree/example-access-transformers) branch for a working example!

__Warning:__ Access Transformers are bugged and will deny you any sources for the decompiled minecraft! Your development environment will still work, but you might face some inconveniences. For example, IntelliJ will not permit searches in dependencies without attached sources.

### Mixins

Mixins are usually used to modify vanilla or mod/library in runtime without having to change source code. For example, redirect a call, change visibility or make class implement your interface. It's an advanced topic and most mods don't need to do that.

You can activate Mixin in 'gradle.properties'. In that case a mixin configuration (usually named `mixins.mymodid.json`) will be generated automatically, and you only have to write the mixins itself. Dependencies are handled as well.
Take a look at the examples in [Hodgepodge](https://github.com/GTNewHorizons/Hodgepodge/) and [Angelica](https://github.com/GTNewHorizons/Angelica/pull/8).

### Advanced

If your project requires custom gradle commands you may add a `addon.gradle[.kts]` to your project. It will be added automatically to the build script. Although we recommend against it, it is sometimes required. When in doubt, feel free to ask us about it. You may break future updates of this build system!
If you need access to properties modified later in the buildscript, you can also use a `addon.late.gradle[.kts]`.
For local tweaks that you don't want to commit to Git, like adding extra JVM arguments for testing, use `addon[.late].local.gradle[.kts]`.

### Feedback wanted

If you tried out this build script we would love to head your opinion! Is there any feature missing for you? Did something not work? Please open an issue and we will try to resolve it asap!

Happy modding, \
[SinTh0r4s](https://github.com/SinTh0r4s), [TheElan](https://github.com/TheElan) and [basdxz](https://github.com/basdxz)
