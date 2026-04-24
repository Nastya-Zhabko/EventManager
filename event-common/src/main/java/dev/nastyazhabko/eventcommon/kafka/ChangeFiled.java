package dev.nastyazhabko.eventcommon.kafka;

public record ChangeFiled(
        String field,
        String oldValue,
        String newValue
) {
}
