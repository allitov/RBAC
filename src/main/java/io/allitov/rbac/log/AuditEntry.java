package io.allitov.rbac.log;

import java.time.LocalDateTime;

public record AuditEntry(LocalDateTime timestamp, String action, String performer, String target, String details) {}
