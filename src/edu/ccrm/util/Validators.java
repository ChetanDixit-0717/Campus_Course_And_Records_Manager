package edu.ccrm.util;

import java.util.regex.Pattern;

public class Validators {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
        "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"
    );

    public static boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isPositive(int number) {
        return number > 0;
    }

    public static boolean isNotNullOrEmpty(String str) {
        return str != null && !str.trim().isEmpty();
    }

}
