import java.util.Scanner;

public class Main
{
    static int MIN(int[] values){
        int min_value = values[0];
        for (int index = 1; index < values.length; index++) {
            if (values[index] < min_value){
                min_value = values[index];
            }

        }
        return min_value;
    }

    static int indexOfMin(int [] values){
        int min = MIN(values);
        for (int index = 0; index < values.length; index++) {
            if (values[index] == min) {
                return index;
            }
        }
        return -1;
    }


    static int countOfMin(int [] values) {
        int result = 1;
        int min_value = MIN(values);
        for(int index = indexOfMin(values) + 1; index < values.length; index ++){
            result += (values[index] == min_value) ? 1 : 0;
        }
        return result;
    }



    public static void main(String[] args) {
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


        if (values.length == 0) {
            System.out.println("Масив має містити елементи");
        } else {
            int result = MIN(values);
            int index_min = indexOfMin(values);
            int count = countOfMin(values);
            System.out.println("Мінімум: " + result + "; Індекс: " + index_min + "; Кількість входжень: " + count);
        }

        scanner.close();
        lineScanner.close();
    }
}