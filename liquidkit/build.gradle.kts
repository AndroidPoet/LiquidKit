import io.github.androidpoet.liquidkit.build.LiquidKitConfiguration
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xcontext-parameters")
    }

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
            api(compose.runtime)
            api(compose.ui)
            implementation(compose.foundation)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }

        androidMain.dependencies {
            implementation(libs.androidx.annotation)
            implementation(libs.jetbrains.annotations)
            implementation(libs.kyant.shapes)
            implementation(libs.kotlinx.coroutines.android)
        }
    }

    targets
        .withType<KotlinNativeTarget>()
        .matching { it.konanTarget.family.isAppleFamily }
        .configureEach {
            binaries.framework {
                baseName = LiquidKitConfiguration.libraryFrameworkName
                isStatic = true
            }
        }
}

android {
    namespace = LiquidKitConfiguration.libraryNamespace
    compileSdk = LiquidKitConfiguration.compileSdk

    defaultConfig {
        minSdk = LiquidKitConfiguration.minSdk
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
