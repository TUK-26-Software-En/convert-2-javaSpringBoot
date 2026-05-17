package org.tukorea.servicemonitor.global.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

@ConfigurationProperties(prefix = "monitoring.docker-control")
public class DockerControlProperties {

    private boolean enabled;
    private String socketPath = "/var/run/docker.sock";
    private List<String> allowedTargets = new ArrayList<>(List.of("library-service", "postgres"));
    private List<String> allowedCommands = new ArrayList<>(List.of(
            "inspect",
            "logs",
            "stats",
            "pause",
            "unpause",
            "start",
            "stop",
            "restart"
    ));

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getSocketPath() {
        return socketPath;
    }

    public void setSocketPath(String socketPath) {
        this.socketPath = socketPath;
    }

    public List<String> getAllowedTargets() {
        return allowedTargets;
    }

    public void setAllowedTargets(List<String> allowedTargets) {
        this.allowedTargets = new ArrayList<>(allowedTargets);
    }

    public List<String> getAllowedCommands() {
        return allowedCommands;
    }

    public void setAllowedCommands(List<String> allowedCommands) {
        this.allowedCommands = new ArrayList<>(allowedCommands);
    }
}
