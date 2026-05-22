import io.github.androidpoet.liquidkit.build.LiquidKitConfiguration
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    sourceSets {
        commonMain.dependencies {
            implementation(project(":liquidkit"))
            implementation(compose.foundation)
            implementation(compose.runtime)
            implementation(compose.ui)
        }

        androidMain.dependencies {
            implementation(libs.activity.compose)
            implementation(libs.androidx.core.ktx)
        }
    }

    targets
        .withType<KotlinNativeTarget>()
        .matching { it.konanTarget.family.isAppleFamily }
        .configureEach {
            binaries.framework {
                baseName = LiquidKitConfiguration.sampleFrameworkName
                isStatic = true
            }
        }
}

android {
    namespace = LiquidKitConfiguration.sampleNamespace
    compileSdk = LiquidKitConfiguration.compileSdk

    defaultConfig {
        applicationId = LiquidKitConfiguration.sampleApplicationId
        minSdk = LiquidKitConfiguration.minSdk
        targetSdk = LiquidKitConfiguration.targetSdk
        versionCode = LiquidKitConfiguration.sampleVersionCode
        versionName = LiquidKitConfiguration.sampleVersionName
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
