package design_patterns.structural.facade_pattern;

public class DeploymentAppFacade {
    public static void main(String[] args) {
        DeploymentFacade deploymentFacade = new DeploymentFacade();

        // Deploy to production
        deploymentFacade.deployApplication("main", "prod.server.example.com");

        // Deploy a feature branch to staging
        System.out.println("\n--- Deploying feature branch to staging ---");
        deploymentFacade.deployApplication("feature/new-ui", "staging.server.example.com");

        // Check deployment status
        deploymentFacade.checkDeploymentStatus("dev.server.example.com");
    }
}