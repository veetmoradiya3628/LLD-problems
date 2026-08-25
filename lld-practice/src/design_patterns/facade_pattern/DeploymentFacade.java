package design_patterns.facade_pattern;

public class DeploymentFacade {
    private final VersionControlSystem vcs = new VersionControlSystem();
    private final BuildSystem buildSystem = new BuildSystem();
    private final TestingFramework testingFramework = new TestingFramework();
    private final DeploymentTarget deploymentTarget = new DeploymentTarget();

    public void deployApplication(String branch, String serverAddress) {
        System.out.println("\nFACADE: --- Initiating FULL DEPLOYMENT for branch: " + branch + " to " + serverAddress + " ---");
        boolean success = true;

        try {
            vcs.pullLatestChanges(branch);

            if (!buildSystem.compileProject()) {
                System.err.println("FACADE: DEPLOYMENT FAILED - Build compilation failed.");
                return;
            }

            String artifactPath = buildSystem.getArtifactPath();

            if (!testingFramework.runUnitTests()) {
                System.err.println("FACADE: DEPLOYMENT FAILED - Unit tests failed.");
                return;
            }

            if (!testingFramework.runIntegrationTests()) {
                System.err.println("FACADE: DEPLOYMENT FAILED - Integration tests failed.");
                return;
            }

            deploymentTarget.transferArtifact(artifactPath, serverAddress);
            deploymentTarget.activateNewVersion(serverAddress);

            System.out.println("FACADE: APPLICATION DEPLOYED SUCCESSFULLY to " + serverAddress + "!");
        } catch (Exception e) {
            System.err.println("FACADE: DEPLOYMENT FAILED - An unexpected error occurred: " + e.getMessage());
            e.printStackTrace();
            success = false;
        }
    }

    public void checkDeploymentStatus(String serverAddress) {
        System.out.println("FACADE: Checking deployment status started");
        deploymentTarget.checkDeploymentStatus(serverAddress);
        System.out.println("FACADE: Checking deployment status finished");
    }
}