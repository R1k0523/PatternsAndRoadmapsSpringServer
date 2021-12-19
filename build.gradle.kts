import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.5.5"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.spring") version "1.5.31"
    kotlin("plugin.serialization") version "1.5.10" apply true
}

group = "boring.owl"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11
java.targetCompatibility = JavaVersion.VERSION_11

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web:2.6.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("junit:junit:4.13.2")

    implementation("org.springframework.boot:spring-boot-starter-data-jpa:2.6.1")
    runtimeOnly("com.h2database:h2:1.4.200")

    testImplementation("org.springframework.boot:spring-boot-starter-test:2.6.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1")
    implementation("com.google.code.gson:gson:2.8.9")

    implementation("io.springfox:springfox-boot-starter:3.0.0")
    runtimeOnly("io.springfox:springfox-swagger-ui:2.9.2")
    implementation("io.jsonwebtoken:jjwt:0.9.1")

    implementation("javax.validation:validation-api:2.0.1.Final")
    implementation("org.hibernate.validator:hibernate-validator:7.0.1.Final")
    implementation("org.springframework.boot:spring-boot-starter-security:2.6.1")
    implementation("org.springframework.security.oauth:spring-security-oauth2:2.5.1.RELEASE")
    testImplementation("org.springframework.security:spring-security-test:5.5.1")

    implementation("com.github.kittinunf.fuel:fuel:2.3.1")
    implementation ("com.google.code.gson:gson:2.8.8")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "11"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

