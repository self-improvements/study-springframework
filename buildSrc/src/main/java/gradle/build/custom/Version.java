package gradle.build.custom;

/**
 * Specification of dependency version in {@code build.gradle}.
 */
public enum Version {

    SPRING_BOOT("2.6.8"),
    JSR305("3.0.2"),
    REFLECTIONS("0.9.12"),
    JWT("0.11.2");

    private final String value;

    Version(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return this.value;
    }

}
