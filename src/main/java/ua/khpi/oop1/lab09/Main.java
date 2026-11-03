package ua.khpi.oop1.lab09;

/**
 * Лабораторна робота № 9. Варіант 1 (обов'язковий рівень).
 * Регулярні вирази не використовуються.
 */
public class Main {

    // Лічильники для протоколу
    static int total = 0;
    static int passed = 0;

    //Обчислення

    /**
     * Замінює кожну послідовність пробільних символів одним пробілом і прибирає пробіли по краях.
     * Вхід не може бути null.
     */
    static String normalize(String source) {
        StringBuilder result = new StringBuilder(source.length());
        boolean separatorPending = false;

        for (int index = 0; index < source.length(); index++) {
            char symbol = source.charAt(index);
            if (Character.isWhitespace(symbol)) {
                // пробіл додаємо лише тоді коли вже є текст
                separatorPending = result.length() > 0;
            } else {
                if (separatorPending) {
                    result.append(' ');
                    separatorPending = false;
                }
                result.append(symbol);
            }
        }
        return result.toString();
    }

    /**
     * Кількість вилучених одиниць UTF-16: різниця довжин початкового і нормалізованого рядків
     */
    static int removedUnits(String source, String normalized) {
        return source.length() - normalized.length();
    }

    // Консольне подання

    static void check(String input, String expected, String actual) {
        total++;
        boolean ok = expected.equals(actual);
        if (ok) {
            passed++;
        }
        System.out.println((ok ? "ОК       " : "ПОМИЛКА  ") + "вхід=" + input + " очікувано=" + expected + " фактично=" + actual);
    }

    /** Перевірка: очікуваний текст і кількість вилучених одиниць */
    static void checkNormalize(String source, String expected, int expectedRemoved) {
        if (source == null) {            // відсутній вхід обробляється явно
            System.out.println("Немає входу: null не допускається");
            return;
        }
        String normalized = normalize(source);
        int removed = removedUnits(source, normalized);
        check("\"" + source + "\"",
                "\"" + expected + "\", " + expectedRemoved,
                "\"" + normalized + "\", " + removed);
    }

    public static void main(String[] args) {
        System.out.println("Варіант 1: normalize");
        checkNormalize("  Java   text  ", "Java text", 6);
        checkNormalize("A\tB \nC ", "A B C", 2);
        checkNormalize("   ", "", 3);
        checkNormalize("sensor ready", "sensor ready", 0);
        checkNormalize("", "", 0);
        checkNormalize("F", "F", 0);
        checkNormalize(" 5", "5", 1);
        checkNormalize("a ", "a", 1);
        checkNormalize("Y   N", "Y N", 2);
        // символ поза базовою площиною (сурогатна пара) не розривається
        checkNormalize(" H\uD83D\uDE0E    I ", "H\uD83D\uDE0E I", 5);
        checkNormalize(null, "", 0);

        System.out.println();
        System.out.println("Пройдено " + passed + " з " + total);
    }
}