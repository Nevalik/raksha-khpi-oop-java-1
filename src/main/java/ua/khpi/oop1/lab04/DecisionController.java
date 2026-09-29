package ua.khpi.oop1.lab04;

public class DecisionController {

    static String classifyChannel(double t) {
        // isFinite(t) повертає false для NaN та нескінченностей.
        // Також відсікаємо значення поза фізичним діапазоном [-50.0; 150.0].

        if (!Double.isFinite(t) || t < -50.0 || t > 150.0) {
            return "invalid";
        } else if (t < 20.0) {
            return "cold";
        } else if (t < 80.0) {
            return "normal";
        } else if (t < 110.0) {
            return "warning";
        } else {
            return "shutdown";
        }
    }

    static int priority(String state) {
        return switch (state) {
            case "shutdown" -> 0;   // Найвищий пріоритет
            case "warning" -> 1;
            case "cold" -> 2;
            case "normal" -> 3;    // Норма (найнижчий пріоритет)
            default -> 99;
        };
    }

    public static void main(String[] args) {
        // Вхідні дані:
        double t1 = 88.8;
        double t2 =58.3;

        String state1 = classifyChannel(t1);
        String state2 = classifyChannel(t2);

        // Перевіряємо, чи є хоча б один некоректний канал
        boolean anyInvalid = state1.equals("invalid") || state2.equals("invalid");

        String result;
        // Логіка вибору підсумкового результату:
        if (anyInvalid) {
            result = "invalid";
        } else if (priority(state1) <= priority(state2)) {
            result = state1;
        } else {
            result = state2;
        }
        // Виведення результатів у консоль з форматуванням
        System.out.printf("Канал 1: %.1f C -> %s%n", t1, state1);
        System.out.printf("Канал 2: %.1f C -> %s%n", t2, state2);
        System.out.println("Спiльний результат: " + result);
    }
}