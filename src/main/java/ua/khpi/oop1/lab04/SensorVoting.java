package ua.khpi.oop1.lab04;

public class SensorVoting {

    static boolean isValid(double t) {
        return Double.isFinite(t) && t >= -50.0 && t <= 150.0;
    }

    static double median3(double a, double b, double c) {
        double max = Math.max(a, Math.max(b, c));
        double min = Math.min(a, Math.min(b, c));
        return a + b + c - max - min;
    }

    static void vote(double s1, double s2, double s3) {
        boolean v1 = isValid(s1), v2 = isValid(s2), v3 = isValid(s3);
        int validCount = (v1?1:0) + (v2?1:0) + (v3?1:0);

        String status;
        double result = Double.NaN;

        if (validCount == 3) {
            result = median3(s1, s2, s3);
            status = "voted";
        } else if (validCount == 2) {
            if (!v1) result = (s2 + s3) / 2.0;
            else if (!v2) result = (s1 + s3) / 2.0;
            else result = (s1 + s2) / 2.0;
            status = "degraded";
        } else {
            status = "failed";
        }

        if (status.equals("failed")) {
            System.out.println("status: failed");
        } else {
            System.out.printf("result=%.2f, status: %s%n", result, status);
        }
    }

    public static void main(String[] args) {
        vote(20, 23, 21);              // усі валідні, будь-який порядок 21, voted
        vote(23, 20, 21);
        vote(21, 20, 23);
        vote(Double.NaN, 20, 24);       // одна відмова - degraded
        vote(Double.NaN, Double.NaN, 20); // дві відмови - failed
        vote(200, Double.NaN, 20);        // дві відмови - failed
        vote(-50.0, -50.0, -50.0);        // нижня межа включена - voted
        vote(150.0, 150.0, 150.0);        // верхня межа включена - voted
        vote(-50.1, 20, 30);               // ліворуч від межі - одна відмова, degraded
    }
}