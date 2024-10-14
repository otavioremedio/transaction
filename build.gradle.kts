plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	kotlin("plugin.jpa") version "1.7.0"
	id("org.springframework.boot") version "3.3.0"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "caju.transaction"
version = "0.0.1-SNAPSHOT"
extra["restAssuredVersion"] = "5.1.1"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.cloud:spring-cloud-starter-openfeign")
	implementation("net.logstash.logback:logstash-logback-encoder:7.2")
	implementation("org.apache.logging.log4j:log4j-api:2.17.2")
	implementation("org.apache.logging.log4j:log4j-to-slf4j:2.17.2")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	testImplementation("org.springframework.cloud:spring-cloud-contract-wiremock:4.1.4")
	testImplementation("com.h2database:h2:2.1.214")
	testImplementation("com.nhaarman.mockitokotlin2:mockito-kotlin:2.2.0")
	testImplementation("io.rest-assured:rest-assured:${property("restAssuredVersion")}")
	testImplementation("io.rest-assured:json-path:${property("restAssuredVersion")}")
	testImplementation("io.rest-assured:xml-path:${property("restAssuredVersion")}")
	testImplementation("io.rest-assured:kotlin-extensions:${property("restAssuredVersion")}")
}

dependencyManagement {
	imports {
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:2023.0.2")
	}
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}