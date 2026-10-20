package ua.khpi.oop1.lab06;

import java.util.Scanner;

public class Main {
    static final int MAX_CODE = 16383;
    static filan double REFERENCE_VOLTS = 3.3;
    static final double OFFSET_VOLTS = 0.5;
    static final double CELSIUS_PER_VOLT = 100.0;

    static boolean isValidVoltage(double voltageVolts) {
        return Double.isFinite(voltageVolts);
    }



    static double voltageFromCode(int code) {
        if (code < 0 || code > MAX_CODE){
            throw new IllegalArgumentException("ADC поза діапазоном 0..16383");
        }
        return code  / MAX_CODE * REFERENCE_VOLTS;
    }


    static double temperatureFromVoltage(double voltage) {
        if (!isValidVoltage(voltage)) {
            throw new IllegalArgumentException("voltage поза діапазоном");
        }
        return (voltage - OFFSET_VOLTS) * CELSIUS_PER_VOLT;
    }

    static double temperatureFromCode(int code) {
        double Voltage = voltageFromCode(code);
        return temperatureFromVoltage(Voltage);
    }


}