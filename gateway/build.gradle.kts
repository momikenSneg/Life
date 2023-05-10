import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.10"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	kotlin("jvm") version "1.7.22"
	kotlin("kapt") version "1.8.10"
	kotlin("plugin.spring") version "1.7.22"
}

group = "com.github.momikensneg.life.gateway"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_16

allprojects {
	repositories {
		mavenCentral()
		maven(url="https://jitpack.io")
	}
}

extra["testcontainersVersion"] = "1.17.6"
extra["springCloudVersion"] = "2021.0.6"

sourceSets["main"].java {
	srcDir("build/generated/source/kapt/main")
}

val openapiVersion = "1.6.15"
val jjwtVersion = "0.11.5"
val reactorVersion = "3.5.2"
val r2dbcVersion = "1.0.1.RELEASE"
val postgresVersion = "42.5.4"
val mapstructVersion = "1.5.3.Final"
val commonVersion = "1.1"
val logbackVersion = "1.4.5"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
	implementation("org.springframework.boot:spring-boot-starter-security")
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.kafka:spring-kafka")
	implementation("org.springframework.cloud:spring-cloud-starter-gateway")
	implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
	implementation("org.springframework.cloud:spring-cloud-bindings:1.8.1")
	implementation("org.springframework.boot:spring-boot-configuration-processor")
	implementation("org.springframework.boot:spring-boot-devtools")
	implementation("org.springdoc:springdoc-openapi-webflux-core:${openapiVersion}")
	implementation("org.springdoc:springdoc-openapi-webflux-ui:${openapiVersion}")
	implementation("io.jsonwebtoken:jjwt-api:${jjwtVersion}")
	runtimeOnly("io.jsonwebtoken:jjwt-impl:${jjwtVersion}")
	runtimeOnly("io.jsonwebtoken:jjwt-jackson:${jjwtVersion}")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
	implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
	implementation("org.liquibase:liquibase-core")
	implementation("org.springframework:spring-jdbc")
	implementation("io.projectreactor:reactor-tools:${reactorVersion}")
	implementation(fileTree(mapOf("dir" to "C:/Users/momik/.m2/repository/com/github/momikensneg/lifelib/1.0.6", "include" to listOf("*.jar"))))
	kapt(fileTree(mapOf("dir" to "C:/Users/momik/.m2/repository/com/github/momikensneg/lifelib/1.0.6", "include" to listOf("*.jar"))))
	implementation("com.squareup:kotlinpoet:1.12.0")
//	implementation("com.github.momikenSneg:LifeLib:1.0.8")
//	kapt("com.github.momikenSneg:LifeLib:1.0.8")
	implementation("org.mapstruct:mapstruct:${mapstructVersion}")
	kapt("org.mapstruct:mapstruct-processor:${mapstructVersion}")
	runtimeOnly("org.postgresql:r2dbc-postgresql:${r2dbcVersion}")
	runtimeOnly("org.postgresql:postgresql:${postgresVersion}")
	testImplementation("org.springframework.boot:spring-boot-starter-test"){
		exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
		exclude(group = "org.mockito", module = "mockito-core")
	}
	testImplementation("org.junit.jupiter:junit-jupiter-engine")
	testImplementation("com.ninja-squad:springmockk:3.1.2")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.springframework.kafka:spring-kafka-test")
	testImplementation("org.testcontainers:junit-jupiter")
	testImplementation("org.testcontainers:postgresql")
	testImplementation("org.testcontainers:r2dbc")
	testImplementation(kotlin("test"))
}

dependencyManagement {
	imports {
		mavenBom("org.testcontainers:testcontainers-bom:${property("testcontainersVersion")}")
		mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
	}
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "16"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

//tasks.test {
//	useJUnitPlatform()
//}
