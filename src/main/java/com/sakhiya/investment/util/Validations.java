
package com.sakhiya.investment.util;

import java.util.regex.Pattern;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Utility class for common validation methods used across the application.
 */
public class Validations {
    /**
     * Validates if the given string is a valid email address using a regex pattern.
     * @param email the email string to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidEmail(String email) {
        if (email == null)
            return false;
        // ^[A-Za-z0-9+_.-]+ → one or more letters, digits, +, _, ., or - (the local part before the @)
        // @ → literal @
        // [A-Za-z0-9.-]+ → one or more letters, digits, . or - (the domain name)
        // \\. → a literal dot (escaped in Java as \\.)
        // [A-Za-z]{2,}$ → at least 2 letters (the top-level domain, e.g. com, org, uk)
        return Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$").matcher(email).find();
    }
    /**
     * Validates if the given string is a valid ISO_LOCAL_DATE (yyyy-MM-dd).
     * Throws IllegalArgumentException if invalid.
     *
     * @param dateString the date string to validate
     * @return true if valid, otherwise throws exception
     */
    public static Boolean isValidDate(String dateString) {
        try {
            // Attempt to parse the input string into a LocalDate using the standard ISO format (yyyy-MM-dd)
            LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
            return true;
        } catch (DateTimeParseException e) {
            // If parsing fails (invalid format or value), throw an exception with a helpful message
            throw new IllegalArgumentException("Invalid date format, expected yyyy-MM-dd");
        }
    }
}
