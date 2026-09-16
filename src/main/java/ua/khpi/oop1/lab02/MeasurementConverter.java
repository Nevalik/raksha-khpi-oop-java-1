package ua.khpi.oop1.lab02;

/**
 * Варіант 1
 * Перетворення градусів Цельсія у Фаренгейти й Кельвіни.
 */
public class MeasurementConverter {

    public static void main(String[] args) {
        System.out.println("Звичайний набір");
        convertAndPrint(25.0);

        System.out.println();
        System.out.println("Мінімальний набір");
        convertAndPrint(-273.15);

        System.out.println();
        System.out.println("Максимальний набір");
        convertAndPrint(150.0);

        System.out.println();
        System.out.println("Ризиковий набір");
        convertAndPrint(-40.0);

        System.out.println();
        System.out.println("Ручна перевірка");
        runManualChecks();
    }

    /**
     * Виконує основне перетворення й виводить проміжні та підсумкові значення
     */
    private static void convertAndPrint(double celsius) {
        //масштабування без зміщення.
        double scaledFahrenheit = celsius * 9.0 / 5.0;

        //додається зміщення шкали.
        double fahrenheit = scaledFahrenheit + 32.0;

        double kelvindiff = 273.15;
        double kelvin = celsius + 273.15;

        System.out.printf("celsius:               %8.2f C%n", celsius);
        System.out.printf("scaledFahrenheit (int):%8.4f%n", scaledFahrenheit);
        System.out.printf("fahrenheit:            %8.2f F%n", fahrenheit);
        System.out.printf("kelvin difference:     %8.2f K%n", kelvindiff);
        System.out.printf("kelvin:                %8.2f K%n", kelvin);
    }

    /**
     * ручні перевірки числових операцій
     */
    private static void runManualChecks() {
        double wrongRatio = 1 / 4;
        double correctRatio = 1.0 / 4;
        System.out.println("Перевірка 1: цілочисельне ділення:");
        System.out.println(wrongRatio);   // 0.0
        System.out.println(correctRatio); // 0.25

        //переповнення проміжного добутку int
        int width = 70_000;
        int height = 70_000;
        long wrongArea = width * height;          // добуток обчислюється як int -> переповнення
        long correctArea = (long) width * height;
        System.out.println("  wrongArea   = " + wrongArea);
        System.out.println("  correctArea = " + correctArea);

        //звуження double -> byte
        double source = 260.75;
        byte narrowed = (byte) source;
        System.out.printf("  source=%.2f, narrowed=%d%n", source, narrowed);

        //десяткова похибка
        double sum = 0.1 + 0.2;
        double difference = Math.abs(sum - 0.3);
        System.out.println("  hex(sum)        = " + Double.toHexString(sum));
        System.out.println("  hex(difference) = " + Double.toHexString(difference));
    }
}