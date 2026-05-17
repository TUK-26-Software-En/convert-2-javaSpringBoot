package org.tukorea.servicemonitor.health.model;

public enum ServiceTarget {
    LIBRARY_SERVICE("Library Service", "library-service"),
    POSTGRES("PostgreSQL", "postgres");

    private final String displayName;
    private final String composeServiceName;

    ServiceTarget(String displayName, String composeServiceName) {
        this.displayName = displayName;
        this.composeServiceName = composeServiceName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getComposeServiceName() {
        return composeServiceName;
    }

    public static ServiceTarget fromComposeServiceName(String composeServiceName) {
        for (ServiceTarget target : values()) {
            if (target.composeServiceName.equalsIgnoreCase(composeServiceName)) {
                return target;
            }
        }
        throw new IllegalArgumentException("Unsupported service target: " + composeServiceName);
    }
}
