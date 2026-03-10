package io.allitov.rbac.util;

import java.util.List;
import java.util.Scanner;
import org.apache.commons.lang3.StringUtils;

@SuppressWarnings("java:S106")
public final class ConsoleUtils {

    public static final String RESET = "\u001B[0m";
    public static final String CYAN = "\u001B[36m";
    public static final String RED = "\u001B[31m";
    public static final String YELLOW = "\u001B[33m";

    private ConsoleUtils() {
        throw new IllegalStateException("No ConsoleUtils instances for you!");
    }

    public static String promptString(Scanner scanner, String message, boolean required) {
        while (true) {
            IO.print(CYAN + ">" + message + (required ? " (обязательно):" : ":") + RESET);
            String input = scanner.nextLine().trim();
            if (!required || !StringUtils.isEmpty(input)) {
                return input;
            }
            IO.println(RED + "Ошибка: поле не может быть пустым." + RESET);
        }
    }

    public static int promptInt(Scanner scanner, String message, int min, int max) {
        while (true) {
            try {
                String input = promptString(scanner, message + " [" + min + "-" + max + "]", true);
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
                IO.println(RED + "Число вне диапазона!" + RESET);
            } catch (NumberFormatException _) {
                IO.println(RED + "Введите корректное целое число." + RESET);
            }
        }
    }

    public static boolean promptYesNo(Scanner scanner, String message) {
        while (true) {
            String input = promptString(scanner, message + " (да/нет): ", true).toLowerCase();
            if (input.equals("да")) {
                return true;
            }
            if (input.equals("нет")) {
                return false;
            }
            IO.println(RED + "Введите 'да' или 'нет'." + RESET);
        }
    }

    public static <T> T promptChoice(Scanner scanner, String message, List<T> options) {
        IO.println(YELLOW + "--- " + message + " ---" + RESET);
        for (int i = 0; i < options.size(); i++) {
            IO.println((i + 1) + ". " + options.get(i));
        }
        int choice = promptInt(scanner, "Выберите номер", 1, options.size());
        return options.get(choice - 1);
    }
}
