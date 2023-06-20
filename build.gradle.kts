plugins {
	java
	id("org.springframework.boot") version "2.7.12"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
}

group = "com.notmorron"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-artemis")
	implementation("org.springframework.boot:spring-boot-starter-logging")

	compileOnly("org.projectlombok:lombok")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("org.testcontainers:testcontainers:1.18.3")
	testImplementation("org.testcontainers:postgresql:1.18.3")
	testImplementation("org.testcontainers:testcontainers:1.18.3")
	testImplementation("org.testcontainers:junit-jupiter:1.18.3")
}

tasks.bootBuildImage{
	imageName = "orderevents"
}

tasks.withType<Test> {
	useJUnitPlatform()
}
