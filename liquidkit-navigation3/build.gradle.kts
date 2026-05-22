import io.github.androidpoet.liquidkit.build.LiquidKitConfiguration
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlinx.serialization)
    id("maven-publish")
}

apply(from = "$rootDir/scripts/publish-module.gradle.kts")

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            api(project(":liquidkit"))
            api(libs.jetbrains.navigation3.ui)
            api(compose.runtime)
            api(compose.ui)
            implementation(compose.foundation)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }
    }

    targets
        .withType<KotlinNativeTarget>()
        .matching { it.konanTarget.family.isAppleFamily }
        .configureEach {
            binaries.framework {
                baseName = "${LiquidKitConfiguration.libraryFrameworkName}Navigation3"
                isStatic = true
            }
        }
}

android {
    namespace = LiquidKitConfiguration.navigation3Namespace
    compileSdk = LiquidKitConfiguration.compileSdk

    defaultConfig {
        minSdk = LiquidKitConfiguration.minSdk
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
