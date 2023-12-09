plugins {
  kotlin("jvm") version "2.0.0-Beta1"
  id("io.gitlab.arturbosch.detekt") version "1.23.4"
}

group = "net.navatwo"
version = "2023"

repositories {
  mavenCentral()
}

detekt {
  config.from("detekt.yml")
  autoCorrect = true
  buildUponDefaultConfig = true
}

dependencies {
  detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.4")

  implementation(kotlin("stdlib"))
  implementation("net.navatwo:kinterval-tree:0.1.1")

  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")

  testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
  testImplementation("org.assertj:assertj-core:3.21.0")
}

fun JavaToolchainSpec.configureJavaToolchain() {
  languageVersion.set(JavaLanguageVersion.of(21))
  vendor.set(JvmVendorSpec.ADOPTIUM)
}

java {
  withJavadocJar()
  withSourcesJar()

  toolchain {
    configureJavaToolchain()
  }
}

kotlin {
  jvmToolchain {
    configureJavaToolchain()
  }
}

tasks.test {
  useJUnitPlatform()
}
