import build.gradle.custom.Dependencies
import org.springframework.boot.gradle.tasks.bundling.BootJar
import java.nio.charset.StandardCharsets

plugins {
    id("java")
    id("org.springframework.boot").version("3.1.1") // Can be replaced with "buildscript.dependencies.classpath".
}

group = "io.github.imsejin.study.springframework"
version = "0.1.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<JavaCompile> {
    options.encoding = StandardCharsets.UTF_8.toString()
    // Supported on Java 8 or higher.
    options.compilerArgs.add("-parameters")
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

subprojects {
    apply {
        plugin("java")
        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        // warning: unknown enum constant When.MAYBE
        // reason: class file for javax.annotation.meta.When not found
        implementation("${Dependencies.JSR305}")

        implementation("${Dependencies.REFLECTIONS}")

        compileOnly("org.projectlombok:lombok")
        annotationProcessor("org.projectlombok:lombok")
        testCompileOnly("org.projectlombok:lombok")
        testAnnotationProcessor("org.projectlombok:lombok")

        annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

        testImplementation("org.springframework.boot:spring-boot-starter-test") {
            exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }
}

// To solve build failure (or you can disable kotlin plugin built in IntelliJ IDEA).
// The following message is from that error.
//
// Task 'prepareKotlinBuildScriptModel' not found in project '?'.
//
// This task has nothing to do with the fact that your project doesn't have Kotlin sources.
// It's about supporting the edition of .gradle.kts scripts in the IDE.
// The fact that you have such scripts or not can't be detected upfront.
// This task needs to run always. When you have no .gradle.kts scripts it doesn't do anything.
// (ref. https://github.com/gradle/gradle/issues/14889)
// tasks.register("prepareKotlinBuildScriptModel")

tasks.withType<BootJar> {
    enabled = false
}

tasks.withType<Wrapper> {
    gradleVersion = "8.2.1"
    distributionType = Wrapper.DistributionType.BIN
}
