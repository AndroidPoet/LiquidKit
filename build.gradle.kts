import com.diffplug.gradle.spotless.SpotlessExtension
import io.github.androidpoet.liquidkit.build.LiquidKitConfiguration
import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.extensions.DetektExtension

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.compose.multiplatform) apply false
    alias(libs.plugins.kotlinx.serialization) apply false
    alias(libs.plugins.vanniktech.publish) apply false
    alias(libs.plugins.spotless) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.dokka) apply false
    alias(libs.plugins.kover)
}

allprojects {
    group = LiquidKitConfiguration.artifactGroup
    version = LiquidKitConfiguration.versionName
}

val ktlintVersion = libs.versions.ktlint.get()
val ktlintOverrides =
    mapOf(
        "ktlint_standard_max-line-length" to "disabled",
        "ktlint_standard_function-signature" to "disabled",
        // Compose @Composable functions use PascalCase by convention.
        "ktlint_standard_function-naming" to "disabled",
    )

// Root-level Spotless for build scripts and misc files.
apply(
    plugin =
        libs.plugins.spotless
            .get()
            .pluginId,
)
configure<SpotlessExtension> {
    kotlinGradle {
        target("*.gradle.kts")
        ktlint(ktlintVersion).editorConfigOverride(ktlintOverrides)
    }
    format("misc") {
        target("*.md", ".gitignore", "config/**/*.yml")
        trimTrailingWhitespace()
        leadingTabsToSpaces()
        endWithNewline()
    }
}

subprojects {
    apply(
        plugin =
            rootProject.libs.plugins.spotless
                .get()
                .pluginId,
    )
    apply(
        plugin =
            rootProject.libs.plugins.detekt
                .get()
                .pluginId,
    )

    configure<SpotlessExtension> {
        kotlin {
            target("src/**/*.kt")
            // The vendored Android Liquid Glass shader engine uses bleeding-edge Kotlin
            // (context parameters) that the ktlint engine cannot yet parse; it is kept
            // as-is and excluded from formatting/linting.
            targetExclude("**/build/**", "**/internal/androidglass/**")
            ktlint(ktlintVersion).editorConfigOverride(ktlintOverrides)
            trimTrailingWhitespace()
            endWithNewline()
        }
        kotlinGradle {
            target("*.gradle.kts")
            ktlint(ktlintVersion).editorConfigOverride(ktlintOverrides)
        }
    }

    configure<DetektExtension> {
        parallel = true
        buildUponDefaultConfig = true
        config.setFrom(rootProject.files("config/detekt/detekt.yml"))
        baseline = file("detekt-baseline.xml")
        source.setFrom(
            files(
                "src/commonMain/kotlin",
                "src/commonTest/kotlin",
                "src/androidMain/kotlin",
                "src/iosMain/kotlin",
                "src/appleMain/kotlin",
            ).filter { it.exists() },
        )
    }

    tasks.withType<Detekt>().configureEach {
        // Use detekt's own embedded analyzer; not all KMP-2.x language features need full type resolution.
        jvmTarget = "17"
        // The vendored shader engine uses context parameters detekt's parser cannot read.
        exclude("**/internal/androidglass/**")
    }
}

dependencies {
    kover(project(":liquidkit"))
    kover(project(":liquidkit-navigation3"))
}

kover {
    reports {
        verify {
            // A regression-guard floor for a UI-heavy KMP library that currently ships
            // no unit-test suite. Ratchet this up as instrumented/unit tests are added.
            rule("Aggregate line coverage") {
                minBound(2)
            }
        }
    }
}
