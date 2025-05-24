plugins {
	java
	id("org.springframework.boot") version "3.4.5"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "com.egon89"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

val langchain4jVersion = "0.36.2"
val tikaVersion = "3.1.0"

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("dev.langchain4j:langchain4j:$langchain4jVersion")
    implementation("dev.langchain4j:langchain4j-ollama:$langchain4jVersion")
    implementation("dev.langchain4j:langchain4j-embeddings-all-minilm-l6-v2:$langchain4jVersion")
    implementation("dev.langchain4j:langchain4j-spring-boot-starter:$langchain4jVersion")
	implementation("dev.langchain4j:langchain4j-reactor:$langchain4jVersion")
	implementation("org.apache.tika:tika-core:$tikaVersion")
	implementation("org.apache.tika:tika-parsers:$tikaVersion")
	implementation("org.apache.tika:tika-parsers-standard-package:$tikaVersion")
	
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
