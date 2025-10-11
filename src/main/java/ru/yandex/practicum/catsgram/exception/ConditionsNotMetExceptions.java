package ru.yandex.practicum.catsgram.exception;

public class ConditionsNotMetExceptions extends RuntimeException {
    public ConditionsNotMetExceptions(String message) {
        super(message);
    }
}
