package ua.khpi.oop1.lab05;

import java.util.Scanner;

public class Main {

    static int negativeCount;
    static long positiveSum;
    static int currentNegativeRun;
    static int longestNegativeRun;


    static int sqrtRoot;
    static int sqrtRemainder;


    static void accept(int value) {
        if (value < 0) {
            negativeCount++;
            currentNegativeRun++;
            if (currentNegativeRun > longestNegativeRun) {
                longestNegativeRun = currentNegativeRun;
            }
        } else {
            currentNegativeRun = 0;
            if (value > 0) {
                positiveSum += value;
            }
        }
    }


    static void computeIntegerSqrt(int n) {
        int remainder = n;
        int odd = 1;
        int root = 0;
        while (odd <= remainder) {
            remainder -= odd;
            odd += 2;
            root++;
        }
        sqrtRoot = root;
        sqrtRemainder = remainder;
    }


    static boolean processVariant1(Scanner in) {
        if (!in.hasNextInt()) {
            System.out.println("Помилка: некоректне введення");
            return false;
        }
        int count = in.nextInt();
        if (count < 0 || count > 20) {
            System.out.println("Помилка: кількість поза діапазоном 0..20");
            return false;
        }
        for (int i = 0; i < count; i++) {
            if (!in.hasNextInt()) {
                System.out.println("Помилка: некоректне або неповне введення");
                return false;
            }
            int value = in.nextInt();
            if (value < -20 || value > 20) {
                System.out.println("Помилка: значення поза діапазоном -20..20");
                return false;
            }
            accept(value);
        }
        System.out.println("Від'ємних значень: " + negativeCount);
        System.out.println("Сума додатних: " + positiveSum);
        System.out.println("Найдовша серія від'ємних: " + longestNegativeRun);
        return true;
    }


    static void processTaskA(Scanner in) {
        if (!in.hasNextInt()) {
            System.out.println("Помилка: некоректне введення");
            return;
        }
        int n = in.nextInt();
        if (n < 0 || n > 10000) {
            System.out.println("Помилка: n поза діапазоном 0..10000");
            return;
        }
        computeIntegerSqrt(n);
        System.out.println("root=" + sqrtRoot + ", remainder=" + sqrtRemainder);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (processVariant1(scanner)) {
            processTaskA(scanner);
        }
    }
}