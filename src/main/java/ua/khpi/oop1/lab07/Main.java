package ua.khpi.oop1.lab07;

import java.util.Arrays;
import java.util.Scanner;

public class Main {

    // Мінімальне значення непорожнього масиву
    static int findMinimum(int[] values) {
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
        String line = scanner.nextLine();

        Scanner lineScanner = new Scanner(line);
        int size = 0;
        while (lineScanner.hasNextInt()) {
            lineScanner.nextInt();
            size++;
        }

        int[] values = new int[size];
        lineScanner = new Scanner(line);
        for (int index = 0; index < size; index++) {
            values[index] = lineScanner.nextInt();
        }

        process(values);
        scanner.close();
        lineScanner.close();


        StableCompaction.runAdvancedLevel();
        System.out.println();
    }
}