package com.crucible.crucible_backend.service;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.api.command.CreateContainerResponse;
import com.github.dockerjava.api.command.WaitContainerResultCallback;
import com.github.dockerjava.api.model.AccessMode;
import com.github.dockerjava.api.model.Bind;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.Volume;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

@Service
public class SandboxOrchestratorService {

    private final DockerClient dockerClient;
    private final String m2RepoPath;
    private final String submissionsBasePath;
    private final int timeoutSeconds;

    public SandboxOrchestratorService(
            DockerClient dockerClient,
            @Value("${crucible.sandbox.m2-repo-path:${user.home}/.m2/repository}") String m2RepoPath,
            @Value("${crucible.sandbox.submissions-path:${user.home}/crucible/submissions}") String submissionsBasePath,
            @Value("${crucible.sandbox.timeout-seconds:45}") int timeoutSeconds) {
        this.dockerClient = dockerClient;
        this.m2RepoPath = m2RepoPath;
        this.submissionsBasePath = submissionsBasePath;
        this.timeoutSeconds = timeoutSeconds;
    }

    /**
     * Spins up an isolated container, volume-mounts the submitted code repository
     * along with a read-only local Maven cache, runs tests offline, and enforces timeouts.
     *
     * @param submissionCodeRef Relative path from submissionsBasePath or an absolute directory path
     * @return Exit code (0 = Pass, >0 = Failed Tests, -1 = Timeout / Error)
     */
    public Integer runAutomatedTests(String submissionCodeRef) {
        String imageName = "maven:3.9-eclipse-temurin-21-alpine";

        // 1. Ensure the image exists locally
        try {
            dockerClient.pullImageCmd(imageName).start().awaitCompletion();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Docker image pull interrupted", e);
        }

        // 2. Resolve and validate directory paths
        Path candidatePath = Paths.get(submissionCodeRef);
        File submissionDir = candidatePath.isAbsolute()
                ? candidatePath.toFile()
                : Paths.get(submissionsBasePath, submissionCodeRef).toFile();

        if (!submissionDir.exists() || !submissionDir.isDirectory()) {
            throw new IllegalArgumentException("Submission directory not found: " + submissionDir.getAbsolutePath());
        }

        File m2Dir = new File(m2RepoPath);
        if (!m2Dir.exists()) {
            m2Dir.mkdirs();
        }

        // 3. Configure container boundaries and volume mounts
        HostConfig hostConfig = HostConfig.newHostConfig()
                .withMemory(256 * 1024 * 1024L) // 256MB RAM cap
                .withNetworkMode("none")       // Isolated network
                .withBinds(
                        new Bind(submissionDir.getAbsolutePath(), new Volume("/workspace")),
                        new Bind(m2Dir.getAbsolutePath(), new Volume("/root/.m2/repository"), AccessMode.ro)
                );

        CreateContainerResponse container = dockerClient.createContainerCmd(imageName)
                .withHostConfig(hostConfig)
                .withWorkingDir("/workspace")
                .withCmd("mvn", "test", "-o")  // Explicit offline execution
                .exec();

        String containerId = container.getId();
        Integer exitCode = -1;

        try {
            // 4. Start execution
            dockerClient.startContainerCmd(containerId).exec();

            // 5. Await test completion with strict timeout
            WaitContainerResultCallback callback = new WaitContainerResultCallback();
            dockerClient.waitContainerCmd(containerId).exec(callback);

            boolean finished = callback.awaitCompletion(timeoutSeconds, TimeUnit.SECONDS);
            if (!finished) {
                dockerClient.killContainerCmd(containerId).exec();
                return -1; // Code timed out or entered an infinite loop
            }

            exitCode = callback.awaitStatusCode();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return -1;
        } finally {
            // 6. Security cleanup guarantee
            dockerClient.removeContainerCmd(containerId).withForce(true).exec();
        }

        return exitCode;
    }
}