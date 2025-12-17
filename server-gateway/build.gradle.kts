plugins {
    kotlin("jvm") version "1.9.20"
    kotlin("plugin.spring") version "1.9.20"
    id("org.springframework.boot") version "3.2.0"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "com.example"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

repositories {
    mavenCentral()
}

extra["springCloudVersion"] = "2023.0.0"

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
        mavenBom("io.opentelemetry.instrumentation:opentelemetry-instrumentation-bom:2.6.0")
    }
}

dependencies {
    implementation("org.springframework.cloud:spring-cloud-starter-gateway")
    implementation("io.opentelemetry.instrumentation:opentelemetry-spring-boot-starter")
    implementation("io.opentelemetry.instrumentation:opentelemetry-logback-mdc-1.0:2.6.0-alpha")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}
