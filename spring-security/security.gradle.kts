import build.gradle.custom.Dependencies

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    // H2 Database
    runtimeOnly("com.h2database:h2")

    // JWT
    implementation("${Dependencies.JWT_API}")
    runtimeOnly("${Dependencies.JWT_IMPL}")
    runtimeOnly("${Dependencies.JWT_JACKSON}")

    testImplementation("org.springframework.security:spring-security-test")
}
