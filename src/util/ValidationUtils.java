package util;

import java.util.regex.Pattern;

public class ValidationUtils {
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[6-9]\\d{9}$");
    private static final Pattern NUMBER_PLATE_PATTERN = Pattern.compile("^[A-Z]{2}[0-9]{2}[A-Z]{2}[0-9]{4}$");

    public static boolean isValidPhone(String phone) {
        return phone != null && PHONE_PATTERN.matcher(phone).matches();
    }

    public static boolean isValidNumberPlate(String numberPlate) {
        return numberPlate != null && NUMBER_PLATE_PATTERN.matcher(numberPlate.toUpperCase()).matches();
    }

    public static boolean isValidName(String name) {
        return name != null && name.trim().length() > 0 && name.trim().length() <= 100;
    }

    public static boolean isValidModel(String model) {
        return model != null && model.trim().length() > 0 && model.trim().length() <= 50;
    }
}
