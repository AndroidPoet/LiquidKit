import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication

val publicationName = when (project.name) {
    "liquidkit-navigation3" -> "LiquidKit Navigation 3"
    else -> "LiquidKit"
}
val publicationDescription = when (project.name) {
    "liquidkit-navigation3" -> "Optional Navigation 3 tab-stack helpers for LiquidKit."
    else -> "A Kotlin Multiplatform component kit for Liquid Glass UI."
}

configure<PublishingExtension> {
    publications.withType<MavenPublication>().configureEach {
        pom {
            name.set(publicationName)
            description.set(publicationDescription)
            url.set("https://github.com/AndroidPoet/LiquidKit")

            licenses {
                license {
                    name.set("The Apache License, Version 2.0")
                    url.set("https://www.apache.org/licenses/LICENSE-2.0.txt")
                }
            }

            developers {
                developer {
                    id.set("AndroidPoet")
                    name.set("AndroidPoet")
                }
            }

            scm {
                connection.set("scm:git:git://github.com/AndroidPoet/LiquidKit.git")
                developerConnection.set("scm:git:ssh://github.com/AndroidPoet/LiquidKit.git")
                url.set("https://github.com/AndroidPoet/LiquidKit")
            }
        }
    }

    repositories {
        maven {
            name = "localBuild"
            url = uri(layout.buildDirectory.dir("repo").get().asFile)
        }
    }
}
