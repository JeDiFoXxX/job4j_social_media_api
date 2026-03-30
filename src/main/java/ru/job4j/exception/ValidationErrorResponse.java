package ru.job4j.exception;

import java.util.List;

public record ValidationErrorResponse(List<Violation> violations) { }
