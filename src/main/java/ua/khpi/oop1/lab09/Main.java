package ua.khpi.oop1.lab09;

/**
 * Лабораторна робота № 9. Варіант 1 (обов'язковий рівень).
 * Регулярні вирази не використовуються.
 */
public class Main {

    static final int STATE_START = 0;
    static final int STATE_PLAIN = 1;
    static final int STATE_QUOTED = 2;
    static final int STATE_AFTER_QUOTE = 3;

    static final int ERROR_UNCLOSED_QUOTE = -1;
    static final int ERROR_QUOTE_IN_PLAIN_FIELD = -2;
    static final int ERROR_TEXT_AFTER_QUOTE = -3;

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

    static int parseCommand(String line, String[] fields) {
        StringBuilder field = new StringBuilder();
        int count = 0;
        int state = STATE_START;

        for (int index = 0; index < line.length(); index++) {
            char symbol = line.charAt(index);

            if (state == STATE_START) {
                if (symbol == ';') {
                    fields[count] = "";
                    count++;
                } else if (symbol == '"') {
                    state = STATE_QUOTED;
                } else {
                    field.append(symbol);
                    state = STATE_PLAIN;
                }
            } else if (state == STATE_PLAIN) {
                if (symbol == ';') {
                    fields[count] = field.toString();
                    count++;
                    field.setLength(0);
                    state = STATE_START;
                } else if (symbol == '"') {
                    return ERROR_QUOTE_IN_PLAIN_FIELD;
                } else {
                    field.append(symbol);
                }
            } else if (state == STATE_QUOTED) {
                if (symbol == '"') {
                    boolean doubledQuote = index + 1 < line.length()
                            && line.charAt(index + 1) == '"';
                    if (doubledQuote) {
                        field.append('"');
                        index++;
                    } else {
                        state = STATE_AFTER_QUOTE;
                    }
                } else {
                    field.append(symbol);
                }
            } else {
                if (symbol == ';') {
                    fields[count] = field.toString();
                    count++;
                    field.setLength(0);
                    state = STATE_START;
                } else {
                    return ERROR_TEXT_AFTER_QUOTE;
                }
            }
        }

        if (state == STATE_QUOTED) {
            return ERROR_UNCLOSED_QUOTE;
        }
        fields[count] = field.toString();
        count++;
        return count;
    }

    static String parseToText(String line) {
        String[] fields = new String[line.length() + 1];
        int count = parseCommand(line, fields);

        if (count == ERROR_UNCLOSED_QUOTE) {
            return "ERROR_UNCLOSED_QUOTE";
        }
        if (count == ERROR_QUOTE_IN_PLAIN_FIELD) {
            return "ERROR_QUOTE_IN_PLAIN_FIELD";
        }
        if (count == ERROR_TEXT_AFTER_QUOTE) {
            return "ERROR_TEXT_AFTER_QUOTE";
        }

        StringBuilder text = new StringBuilder();
        for (int index = 0; index < count; index++) {
            text.append('[').append(fields[index]).append(']');
        }
        return text.toString();
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

    static void checkCommand(String line, String expected) {
        if (line == null) {
            System.out.println("Немає входу: null не допускається");
            return;
        }
        check(line, expected, parseToText(line));
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
        System.out.println("Завдання A: parseCommand");
        checkCommand("a;b;c", "[a][b][c]");
        checkCommand("a;;c", "[a][][c]");
        checkCommand("\"a;b\";c", "[a;b][c]");
        checkCommand("\"a\"\"b\";c", "[a\"b][c]");
        checkCommand("\"abc;d", "ERROR_UNCLOSED_QUOTE");
        checkCommand("\"\"\"", "ERROR_UNCLOSED_QUOTE");
        checkCommand("", "[]");
        checkCommand("Р", "[Р]");
        checkCommand(";", "[][]");
        checkCommand("a;", "[a][]");
        checkCommand(";a", "[][a]");
        checkCommand("\"\"", "[]");
        checkCommand("a\"b;c", "ERROR_QUOTE_IN_PLAIN_FIELD");
        checkCommand("\"a\"b;c", "ERROR_TEXT_AFTER_QUOTE");
        checkCommand("\"\"\"\"", "[\"]");
        checkCommand("\"a\";", "[a][]");
        checkCommand("a;\"\";b", "[a][][b]");
        checkCommand(";\"a;b\"", "[][a;b]");
        checkCommand(null, "");

        System.out.println();
        System.out.println("Пройдено " + passed + " з " + total);
    }
}