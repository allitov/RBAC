package io.allitov.rbac.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class FormatUtils {

    private FormatUtils() {
        throw new IllegalStateException("No FormatUtils instances for you!");
    }

    public static String formatBox(String text) {
        String line = "─".repeat(text.length() + 2);
        return "┌" + line + "┐\n" + "│ " + text + " │\n" + "└" + line + "┘";
    }

    public static String formatHeader(String text) {
        return "\n--- " + text.toUpperCase() + " ---\n";
    }

    public static String truncate(String text, int maxLength) {
        if (text == null || text.length() <= maxLength) return text;
        return text.substring(0, Math.max(0, maxLength - 3)) + "...";
    }

    public static String padRight(String text, int length) {
        return String.format("%-" + length + "s", truncate(text, length));
    }

    public static String padLeft(String text, int length) {
        return String.format("%" + length + "s", truncate(text, length));
    }

    public static String formatTable(String[] headers, List<String[]> rows) {
        int[] widths = new int[headers.length];
        for (int i = 0; i < headers.length; i++) {
            widths[i] = headers[i].length();
            for (String[] row : rows) {
                if (row[i] != null) widths[i] = Math.max(widths[i], row[i].length());
            }
        }

        StringBuilder sb = new StringBuilder();
        String divider =
                "+" + IntStream.of(widths).mapToObj(w -> "-".repeat(w + 2)).collect(Collectors.joining("+")) + "+";

        sb.append(divider).append("\n|");
        for (int i = 0; i < headers.length; i++)
            sb.append(" ").append(padRight(headers[i], widths[i])).append(" |");
        sb.append("\n").append(divider).append("\n");

        for (String[] row : rows) {
            sb.append("|");
            for (int i = 0; i < row.length; i++)
                sb.append(" ")
                        .append(padRight(row[i] != null ? row[i] : "", widths[i]))
                        .append(" |");
            sb.append("\n");
        }
        sb.append(divider);
        return sb.toString();
    }
}
