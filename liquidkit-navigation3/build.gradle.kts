import io.github.androidpoet.liquidkit.build.LiquidKitConfiguration
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.android.library)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.kotlinx.serialization)
    alias(libs.plugins.vanniktech.publish)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kover)
}

mavenPublishing {
    coordinates(
        groupId = LiquidKitConfiguration.artifactGroup,
        artifactId = LiquidKitConfiguration.navigation3ArtifactId,
        version = LiquidKitConfiguration.versionName,
    )
    // Sonatype host (CENTRAL_PORTAL) is configured via the SONATYPE_HOST gradle property.
    pom {
        name.set("LiquidKit Navigation 3")
        description.set("Optional Navigation 3 tab-stack helpers for LiquidKit.")
    }
    if (project.findProperty("RELEASE_SIGNING_ENABLED")?.toString()?.toBoolean() == true &&
        project.findProperty("signingInMemoryKey") != null
    ) {
        signAllPublications()
    }
}

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
