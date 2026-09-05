package com.crucible.crucible_backend.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.WaitContainerResultCallback;
import com.github.dockerjava.api.model.HostConfig;
import org.springframework.stereotype.Service;

@Service
public class SandboxOrchestratorService {

    private final DockerClient dockerClient;

    // Spring's IoC container automatically injects your DockerConfig bean here
    public SandboxOrchestratorService(DockerClient dockerClient) {
        this.dockerClient = dockerClient;
    }

    /**
     * Spins up an isolated container, runs a mocked test command, and returns the exit code.
     * Exit Code 0 = Pass. Anything else = Fail.
     */
    public Integer runAutomatedTests(String submissionCodeRef) {
        String imageName = "alpine:latest"; // A very lightweight Linux distribution

        // 1. Ensure the image exists locally (Pulls it from Docker Hub if missing)
        try {
            dockerClient.pullImageCmd(imageName).start().awaitCompletion();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Docker image pull interrupted", e);
        }

        // 2. Create the isolated container with strict resource limits
        HostConfig hostConfig = HostConfig.newHostConfig()
                .withMemory(256 * 1024 * 1024L) // Hard cap: 256MB RAM
                .withNetworkMode("none");       // No internet access for the rogue code

        CreateContainerResponse container = dockerClient.createContainerCmd(imageName)
                .withHostConfig(hostConfig)
                // For this milestone, we simulate running a test suite that passes
                .withCmd("sh", "-c", "echo 'Running automated tests...' && exit 0")
                .exec();

        String containerId = container.getId();
        Integer exitCode = -1;

        try {
            // 3. Start the container
            dockerClient.startContainerCmd(containerId).exec();

            // 4. Wait for the tests to finish and capture the exit code
            exitCode = dockerClient.waitContainerCmd(containerId)
                    .exec(new WaitContainerResultCallback())
                    .awaitStatusCode();

        } finally {
            // 5. The Security Guarantee: ALWAYS kill and remove the container
            dockerClient.removeContainerCmd(containerId).withForce(true).exec();
        }

        return exitCode;
    }
}