package ua.khpi.oop1.lab12;

/**
 * Пристрій реєстру: незмінний додатний ідентифікатор і потужність у ватах.
 */
final class Device {
    private final int id;
    private final double power;

    Device(int id, double power) {
        if (id <= 0 || !Double.isFinite(power) || power < 0.0) {
            throw new IllegalArgumentException("недійсні дані пристрою");
        }
        this.id = id;
        this.power = power;
    }

    int id() {
        return id;
    }

    double power() {
        return power;
    }
}