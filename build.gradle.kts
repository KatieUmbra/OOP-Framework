import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.dokka.gradle.DokkaTask
import java.net.URL

private val v = "1.0"

plugins {
    kotlin("jvm") version "1.8.10"
    id("java-library")
    id("maven-publish")
    id("signing")
    id("org.jetbrains.dokka") version "1.7.20"
    id("com.github.johnrengelman.shadow") version "4.0.3"
}

group = "gay.kanwi"
version = v

repositories {
    mavenCentral()
    maven {
        url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
    }
}

dependencies {
    //implementation("com.github.jengelman.gradle.plugins:shadow:4.0.3")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.8.10")
    implementation("io.github.classgraph:classgraph:4.8.154")
    implementation("org.jetbrains.kotlin:kotlin-stdlib")
    testImplementation(kotlin("test"))
}

// Tasks

tasks.named<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "11"
}

tasks.register<Jar>("dokkaHtmlJar") {
    dependsOn(tasks.dokkaHtml)
    from(tasks.dokkaHtml.flatMap { it.outputDirectory })
    archiveClassifier.set("html-docs")
}

tasks.register("javadocJar", Jar::class) {
    dependsOn(tasks.dokkaHtml)
    from(tasks.dokkaJavadoc.flatMap { it.outputDirectory })
    archiveClassifier.set("javadoc")
}

tasks.withType<DokkaTask>().configureEach {
    moduleName.set(project.name)
    moduleVersion.set(project.version.toString())
    outputDirectory.set(file("build/dokka/$name"))
    failOnWarning.set(false)
    suppressObviousFunctions.set(true)
    suppressInheritedMembers.set(false)
    offlineMode.set(false)
    dokkaSourceSets.configureEach {
        displayName.set(name)
        suppress.set(displayName.get() != "main")
        reportUndocumented.set(false)
        skipEmptyPackages.set(true)
        skipDeprecated.set(false)
        suppressGeneratedFiles.set(true)
        jdkVersion.set(8)
        languageVersion.set("1.8")
        apiVersion.set("1.8")
        noStdlibLink.set(false)
        noJdkLink.set(false)
        noAndroidSdkLink.set(false)
        includes.from(project.files(), "Module.md")
        sourceRoots.from(file("src/main/kotlin"))
        samples.from(project.files(), "src/samples/kotlin/Basic.kt")
        sourceLink {
            localDirectory.set(file("src/main/kotlin"))
            remoteUrl.set(URL("https://github.com/KatieUmbra/OOP-Framework/tree/main/src/main/kotlin"))
            remoteLineSuffix.set("#L")
        }
    }
}
// Java settings

java {
    withSourcesJar()
    withJavadocJar()
}

// Publishing

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            groupId = group.toString()
            artifactId = "OOP-Framework"
            version = "1.0"

            from(components["java"])

            pom {
                name.set("OOP-Framework")
                description.set("A framework for object-oriented programming excersices.")
                url.set("https://github.com/KatieUmbra/OOP-Framework")
                inceptionYear.set("2021")

                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://github.com/KatieUmbra/OOP-Framework/blob/main/LICENSE")
                    }
                }

                developers {
                    developer {
                        id.set("Kanwi")
                        name.set("Katherine Chesterfield")
                        email.set("business@kanwi.gay")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/KatieUmbra/OOP-Framework.git")
                    developerConnection.set("scm:git:ssh://github.com/KatieUmbra/OOP-Framework.git")
                    url.set("https://github.com/KatieUmbra/OOP-Framework")
                }
            }

            repositories {
                maven {
                    name = "OSSRH"
                    url = uri("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
                    credentials {
                        username = project.properties["ossrhUsername"].toString()
                        password = project.properties["ossthPassword"].toString()
                    }
                }
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}