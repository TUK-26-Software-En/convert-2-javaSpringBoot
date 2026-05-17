package org.tukorea.servicemonitor.dockercontrol.model;

import java.util.Locale;

public enum DockerControlCommand {
    INSPECT(false),
    LOGS(false),
    STATS(false),
    PAUSE(true),
    UNPAUSE(true),
    START(true),
    STOP(true),
    RESTART(true);

    private final boolean mutating;

    DockerControlCommand(boolean mutating) {
        this.mutating = mutating;
    }

    public boolean isMutating() {
        return mutating;
    }

    public String commandText() {
        return name().toLowerCase(Locale.ROOT);
    }
}
