import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.maven.MavenPublication

configure<PublishingExtension> {
    publications.withType<MavenPublication>().configureEach {
        pom {
            name.set("LiquidKit")
            description.set("A Kotlin Multiplatform component kit for Liquid Glass UI.")
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
