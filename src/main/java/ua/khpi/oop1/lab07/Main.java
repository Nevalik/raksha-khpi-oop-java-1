package ua.khpi.oop1.lab07;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    // Мінімальне значення непорожнього масиву
    static int findMinimum(int[] values) {
        if (values.length == 0) {
            throw new IllegalArgumentException("Масив пустий");
        }
        int minimum = values[0];
        for (int index = 1; index < values.length; index++) {
            if (values[index] < minimum) {
                minimum = values[index];
            }
        }
        return minimum;
    }

    // Індекс першого мінімуму
    static int indexOfMinimum(int[] values) {
        if (values.length == 0) {
            return -1;
        }
        int minimum = findMinimum(values);
        for (int index = 0; index < values.length; index++) {
            if (values[index] == minimum) {
                return index;
            }
        }
        return -1;
    }

    // Кількість входжень мінімуму
    static int countOfMinimum(int[] values) {
        if (values.length == 0) {
            return 0;
        }
        int minimum = findMinimum(values);
        int count = 0;
        for (int index = 0; index < values.length; index++) {
            if (values[index] == minimum) {
                count++;
            }
        }
        return count;
    }

    // Новий масив у початковому порядку без усіх мінімумів; вхід не змінюється
    static int[] withoutMinimum(int[] values) {
        if (values.length == 0) {
            return new int[0];
        }
        int minimum = findMinimum(values);
        int[] result = new int[values.length - countOfMinimum(values)];
        int write = 0;
        for (int read = 0; read < values.length; read++) {
            if (values[read] != minimum) {
                result[write] = values[read];
                write++;
            }
        }
        return result;
    }

    // Поелементне порівняння двох масивів
    static boolean isSame(int[] first, int[] second) {
        if (first.length != second.length) {
            return false;
        }
        for (int index = 0; index < first.length; index++) {
            if (first[index] != second[index]) {
                return false;
            }
        }
        return true;
    }

    static void process(int[] values) {
        int[] original = values.clone();
        System.out.println("Вхід: " + Arrays.toString(values));

        if (values.length == 0) {
            System.out.println("Мінімум відсутній");
            System.out.println("Новий масив: " + Arrays.toString(withoutMinimum(values)));
            System.out.println();
            return;
        }

        int minimum = findMinimum(values);
        int minimumIndex = indexOfMinimum(values);
        System.out.println("Крок 1: min=" + minimum + ", перший індекс=" + minimumIndex);

        int count = countOfMinimum(values);
        System.out.println("Крок 2: кількість входжень=" + count
                + ", розмір нового масиву=" + (values.length - count));

        int[] filtered = withoutMinimum(values);
        System.out.println("Крок 3: новий масив=" + Arrays.toString(filtered));

        System.out.println("Вхід після обробки: " + Arrays.toString(values));
        System.out.println("Вхід не змінено: " + isSame(values, original));
        System.out.println();
    }

    public static void main(String[] args) {
        StableCompaction.runTaskA();
        System.out.println();

        // Тести з умови
        process(new int[]{4, 1, 3, 1});
        process(new int[]{2, 2});
        process(new int[]{});

        process(new int[]{7});
        process(new int[]{-5, 3, -5, 8});
        process(new int[]{9, 4, 6, 2});

        // Введення з клавіатури
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введіть елементи через пробіл: ");
        if (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            Scanner lineScanner = new Scanner(line);
            int size = 0;
            while (lineScanner.hasNextInt()) {
                lineScanner.nextInt();
                size++;
            }
            lineScanner.close();

            int[] values = new int[size];
            lineScanner = new Scanner(line);
            for (int index = 0; index < size; index++) {
                values[index] = lineScanner.nextInt();
            }
            lineScanner.close();

            process(values);
            }
        scanner.close();


    }
}


class StableCompaction {

    private static int passed = 0;
    private static int total = 0;

    // Переносить коректні покази на початок масиву, повертає логічний розмір.
    // Для null повертає -1. Якщо trace == true, друкує стан після кожного кроку.
    static int compact(int[] readings, boolean trace) {
        if (readings == null) {
            return -1;
        }
        int write = 0;
        for (int read = 0; read < readings.length; read++) {
            if (readings[read] != Integer.MIN_VALUE) {
                readings[write] = readings[read];
                write++;
            }
            if (trace) {
                printStep("read=" + read + ", write=" + write, readings);
            }
        }
        for (int index = write; index < readings.length; index++) {
            readings[index] = Integer.MIN_VALUE;
        }
        if (trace) {
            printStep("після заповнення хвоста", readings);
        }
        return write;
    }

    // Друкує один рядок трасування
    static void printStep(String label, int[] readings) {
        System.out.print("  " + label + ": ");
        printArray(readings, readings.length);
    }

    // Друкує перші count елементів масиву; пропуск показується як MIN
    static void printArray(int[] values, int count) {
        System.out.print("[");
        for (int index = 0; index < count; index++) {
            if (index > 0) {
                System.out.print(", ");
            }
            if (values[index] == Integer.MIN_VALUE) {
                System.out.print("MIN");
            } else {
                System.out.print(values[index]);
            }
        }
        System.out.println("]");
    }

    // Чи збігаються перші expected.length елементів actual з expected
    // (потребує actual.length >= expected.length)
    static boolean startsWith(int[] actual, int[] expected) {
        for (int index = 0; index < expected.length; index++) {
            if (actual[index] != expected[index]) {
                return false;
            }
        }
        return true;
    }

    // Ущільнює масив, друкує результат і порівнює його з очікуваним.
    // Перевіряються дві незалежні властивості:
    // 1) розмір і логічна частина збігаються з expected (значення та порядок);
    // 2) увесь хвіст [size, length) складається з MIN_VALUE.
    static boolean check(int[] readings, int[] expected) {
        System.out.print("Вхід: ");
        printArray(readings, readings.length);

        int size = compact(readings, false);

        System.out.println("size=" + size);
        System.out.print("Логічна частина: ");
        printArray(readings, size);
        System.out.print("Весь масив: ");
        printArray(readings, readings.length);

        boolean sizeOk = size == expected.length;
        boolean logicalOk = sizeOk && startsWith(readings, expected);
        boolean tailOk = true;
        for (int index = size; index < readings.length; index++) {
            if (readings[index] != Integer.MIN_VALUE) {
                tailOk = false;
            }
        }

        boolean ok = logicalOk && tailOk;
        System.out.print("Очікувано: ");
        printArray(expected, expected.length);
        System.out.println("Розмір: " + sizeOk + ", логічна частина: " + logicalOk
                + ", хвіст: " + tailOk + " -> " + (ok ? "OK" : "FAIL"));
        System.out.println();

        total++;
        if (ok) {
            passed++;
        }
        return ok;
    }

    // Перевірка реакції на null
    static void checkNull() {
        int size = compact(null, false);
        boolean ok = size == -1;
        System.out.println("null: size=" + size + " (очікувано -1) -> " + (ok ? "OK" : "FAIL"));
        System.out.println();
        total++;
        if (ok) {
            passed++;
        }
    }

    static void runTaskA() {
        int missing = Integer.MIN_VALUE;

        System.out.println("=== Завдання A ===");

        // Проміжні кроки для одного набору (той самий compact, лише з trace)
        int[] sample = {12, missing, 8, missing, 8};
        System.out.print("Вхід: ");
        printArray(sample, sample.length);
        int size = compact(sample, true);
        System.out.println("size=" + size);
        System.out.print("Логічна частина: ");
        printArray(sample, size);
        System.out.println();

        // Автоматичні перевірки
        check(new int[]{12, missing, 8, missing, 8}, new int[]{12, 8, 8});
        check(new int[]{}, new int[]{});                                           // порожній
        check(new int[]{5, 7, 9}, new int[]{5, 7, 9});                             // без пропусків
        check(new int[]{missing, missing}, new int[]{});                           // лише пропуски
        check(new int[]{missing, missing, 4, missing, missing, 6}, new int[]{4, 6}); // сусідні пропуски
        check(new int[]{7}, new int[]{7});                                         // один елемент
        check(new int[]{3, 1, missing}, new int[]{3, 1});                          // пропуск у кінці
        checkNull();

        System.out.println("Підсумок завдання A: " + passed + " з " + total + " перевірок OK");
    }
}