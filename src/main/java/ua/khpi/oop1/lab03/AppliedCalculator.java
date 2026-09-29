package ua.khpi.oop1.lab03;

public class AppliedCalculator {

    public static void main(String[] args) {
        runVariant1();
        System.out.println();
        runTask1RingEncoder();
    }

    //Варіант 1: Двоетапний рух
    private static void runVariant1() {
        System.out.println(" Варіант 1: Двоетапний рух ");

        double initialVelocityMps = 5.0;
        double accelerationMps2 = 2.5;
        double stage1TimeSeconds = 4.0;
        double stage2TimeSeconds = 3.0;

        double stage1TimeSquared = stage1TimeSeconds * stage1TimeSeconds;

        double stage1DisplacementMeters =
                initialVelocityMps * stage1TimeSeconds
                        + 0.5 * accelerationMps2 * stage1TimeSquared;

        double velocityAfterStage1Mps =
                initialVelocityMps + accelerationMps2 * stage1TimeSeconds;

        double stage2DisplacementMeters =
                velocityAfterStage1Mps * stage2TimeSeconds;

        double totalDisplacementMeters =
                stage1DisplacementMeters + stage2DisplacementMeters;

        double finalVelocityMps = velocityAfterStage1Mps;

        System.out.printf("шлях на 1 етапі: %.3f m%n", stage1DisplacementMeters);
        System.out.printf("швидкість наприкінці першого етапу: %.3f m/s%n", velocityAfterStage1Mps);
        System.out.printf("шлях на 2 етапі: %.3f m%n", stage2DisplacementMeters);
        System.out.printf("підсумкова відстань: %.3f m%n", totalDisplacementMeters);
        System.out.printf("фінальна швидкість: %.3f m/s%n", finalVelocityMps);

        // якщо a == 0 то рух зводиться до рівномірного
        // v0 * (t1 + t2)
        double control_check = initialVelocityMps * (stage1TimeSeconds + stage2TimeSeconds);
        System.out.printf("перевірка (a=0 видасть): %.3f m%n", control_check);
    }

    //Задача 1: Кільцевий енкодер
    private static void runTask1RingEncoder() {
        System.out.println("Задача 1: Кільцевий енкодер");

        int[][] testPairs = {
                {100, 140},
                {4090, 6},
                {6, 4090},
                {512, 512}
        };

        for (int[] pair : testPairs) {
            int from = pair[0];
            int to = pair[1];
            int clockwiseSteps = RingEncoder.clockwiseSteps(from, to);
            int counterclockwiseSteps = RingEncoder.counterclockwiseSteps(from, to);

            System.out.printf(
                    "from=%d to=%d -> за годинниковою=%d, проти годинникової=%d%n",
                    from, to, clockwiseSteps, counterclockwiseSteps);
        }
    }
}

/**
 * кільцевий енкодер із 4096 положень (0-4095).
 * номери положень зростають за годинниковою стрілкою.
 */
class RingEncoder {
    static final int POSITIONS = 4096;

    static int clockwiseSteps(int from, int to) {
        int rawDifference = to - from;
        return ((rawDifference % POSITIONS) + POSITIONS) % POSITIONS;
    }

    static int counterclockwiseSteps(int from, int to) {
        int clockwise = clockwiseSteps(from, to);
        return (POSITIONS - clockwise) % POSITIONS;
    }
}