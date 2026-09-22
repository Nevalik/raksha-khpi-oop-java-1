package ua.khpi.oop1.lab01;

public class EnvironmentReport {
    public static void main(String[] args) {
        String javaVersion = System.getProperty("java.version");
        String javaVendor = System.getProperty("java.vendor");
        String operatingSystem = System.getProperty("os.name");
        String currentUser = System.getProperty("user.name");
        String workingDirectory = System.getProperty("user.dir");

        System.out.println("Java version: " + javaVersion);
        System.out.println("Java vendor: " + javaVendor);
        System.out.println("Operating system: " + operatingSystem);
        System.out.println("User: " + currentUser);
        System.out.println("Working directory: " + workingDirectory);
    }
}