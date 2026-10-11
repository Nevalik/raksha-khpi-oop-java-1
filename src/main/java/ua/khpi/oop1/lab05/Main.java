package ua.khpi.oop1.lab05;

import java.util.Scanner;

public class Main {

    static int negativeCount;
    static long positiveSum;
    static int currentNegativeRun;
    static int longestNegativeRun;


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

    public static void main(String[] args) {
        processVariant1(new java.util.Scanner(System.in));
    }
}