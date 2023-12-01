plugins {
    kotlin("jvm") version "2.0.0-Beta1"
}

group = "net.navatwo"
version = "2023"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))

    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testImplementation("org.assertj:assertj-core:3.21.0")
}

tasks.test {
    useJUnitPlatform()
}
