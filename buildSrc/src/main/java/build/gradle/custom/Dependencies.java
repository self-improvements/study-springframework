package build.gradle.custom;

/**
 * Specification of dependency version in {@code build.gradle}.
 */
public enum Dependencies {

    JSR305("com.google.code.findbugs","jsr305", "3.0.2"),
    REFLECTIONS("org.reflections","reflections", "0.9.12"),
    JWT_API("io.jsonwebtoken", "jjwt-api", "0.11.2"),
    JWT_IMPL("io.jsonwebtoken", "jjwt-impl", "0.11.2"),
    JWT_JACKSON("io.jsonwebtoken", "jjwt-jackson", "0.11.2");

    private final String groupId;
    private final String artifactId;
    private final String version;

    Dependencies(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    @Override
    public String toString() {
        return groupId + ':' + artifactId + ':' + version;
    }

}
