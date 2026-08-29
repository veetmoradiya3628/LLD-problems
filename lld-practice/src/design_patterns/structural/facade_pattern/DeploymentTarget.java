package design_patterns.structural.facade_pattern;

import java.util.Random;

public class DeploymentTarget {
    public void transferArtifact(String artifactPath, String server) {
        System.out.println("Deployment: Transferring " + artifactPath + " to " + server + "...");
        simulateDelay(1000);
        System.out.println("Deployment: Transfer complete.");
    }

    public void activateNewVersion(String server) {
        System.out.println("Deployment: Activating new version on " + server + "...");
        simulateDelay(500);
        System.out.println("Deployment: Now live on " + server + "!");
    }

    public void checkDeploymentStatus(String server) {
        System.out.println("Deployment: Checking the target environment on " + server + "...");
        simulateDelay(1000);
        Random random = new Random();
        boolean status = random.nextBoolean();
        System.out.println("Deployment: deployment is " + (status ? " passing " : " failing ") + " in target server " + server);
    }

    private void simulateDelay(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}