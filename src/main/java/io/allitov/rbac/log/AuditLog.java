package io.allitov.rbac.log;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.Strings;

public class AuditLog {

    private static final List<AuditEntry> ENTRIES = new ArrayList<>();

    private AuditLog() {
        throw new IllegalStateException("No AuditLog instance for you!");
    }

    public static void log(String action, String performer, String target, String details) {
        ENTRIES.add(new AuditEntry(LocalDateTime.now(), action, performer, target, details));
    }

    public static List<AuditEntry> getAll() {
        return List.copyOf(ENTRIES);
    }

    public static List<AuditEntry> getByPerformer(String performer) {
        return ENTRIES.stream()
                .filter(e -> Strings.CS.equals(e.performer(), performer))
                .toList();
    }

    public static List<AuditEntry> getByAction(String action) {
        return ENTRIES.stream()
                .filter(e -> Strings.CS.equals(e.action(), action))
                .toList();
    }

    public static void printLog() {
        System.out.println("--- Журнал аудита ---");
        ENTRIES.forEach(e -> System.out.printf(
                "[%s] Action: %s | Performer: %s | Target: %s | Details: %s%n",
                e.timestamp(), e.action(), e.performer(), e.target(), e.details()));
    }
}
