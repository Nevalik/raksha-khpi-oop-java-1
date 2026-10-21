package ua.khpi.oop1.lab06;

import java.util.Scanner;

public class Main {
    // Максимальний код 14-бітного АЦП
    static final int MAX_CODE = 16383;
    // Опорна напруга, В
    static final double REFERENCE_VOLTS = 3.3;
    // Зсув напруги датчика, В
    static final double OFFSET_VOLTS = 0.5;
    // Градусів Цельсія на 1 В
    static final double CELSIUS_PER_VOLT = 100.0;

    // Напруга коректна, якщо це скінченне число
    static boolean isValidVoltage(double voltageVolts) {
        return Double.isFinite(voltageVolts);
    }

    // Код коректний, якщо лежить у діапазоні 0..MAX_CODE
    static boolean isValidCode(int code) {
        return code >= 0 && code <= MAX_CODE;
    }

    // Код АЦП -> напруга (В)
    static double voltageFromCode(int code) {
        if (!isValidCode(code)) {
            throw new IllegalArgumentException("ADC поза діапазоном 0..16383");
        }
        return (double) code  / MAX_CODE * REFERENCE_VOLTS;
    }


    // Напруга (В) -> температура (°C)
    static double temperatureFromVoltage(double voltage) {
        if (!isValidVoltage(voltage)) {
            throw new IllegalArgumentException("voltage поза діапазоном");
        }
        return (voltage - OFFSET_VOLTS) * CELSIUS_PER_VOLT;
    }

    // Код АЦП -> температура (°C)
    static double temperatureFromCode(int code) {
        double Voltage = voltageFromCode(code);
        return temperatureFromVoltage(Voltage);
    }


    public static void main(String[] args) {
        // Самоперевірка на відомих значеннях
        System.out.printf("код=0     -> %.2f C (очікувано -50.00)%n", temperatureFromCode(0));
        System.out.printf("код=5000  -> %.2f C (очікувано 50.71)%n", temperatureFromCode(5000));
        System.out.printf("код=16383 -> %.2f C (очікувано 280.00)%n", temperatureFromCode(16383));
        System.out.println("код=-1    вірний? " + isValidCode(-1) + " (очікувано false)");
        System.out.println("код=16384 вірний? " + isValidCode(16384) + " (очікувано false)");

        // Введення коду користувачем
        Scanner scanner = new Scanner(System.in);
        System.out.print("Уведіть АЦП код (0-16383): ");
        if (!scanner.hasNextInt()) {
            System.out.println("помилка введення");
            return;
        }
        int code = scanner.nextInt();
        if (!isValidCode(code)) {
            System.out.println("код поза діапазоном 0-16383");
            return;
        }
        // Розрахунок і виведення результату
        double voltageVolts = voltageFromCode(code);
        System.out.printf("напруга=%.4f V, температура=%.2f C%n",
                voltageVolts, temperatureFromVoltage(voltageVolts));
    }


}