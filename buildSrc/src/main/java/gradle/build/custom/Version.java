package gradle.build.custom;

/**
 * Specification of dependency version in {@code build.gradle}.
 */
public enum Version {

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
