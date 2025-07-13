package util;

import exception.GarageException;

public class InputValidator {

    public static void validateCustomerData(String name, String phone) throws GarageException {
        if (!ValidationUtils.isValidName(name)) {
            throw new GarageException("Invalid customer name. Name must be between 1-100 characters.");
        }

        if (!ValidationUtils.isValidPhone(phone)) {
            throw new GarageException("Invalid phone number. Please enter a valid 10-digit Indian mobile number.");
        }
    }

    public static void validateVehicleData(String numberPlate, String model) throws GarageException {
        if (!ValidationUtils.isValidNumberPlate(numberPlate)) {
            throw new GarageException("Invalid number plate format. Expected format: AB12CD3456");
        }

        if (!ValidationUtils.isValidModel(model)) {
            throw new GarageException("Invalid vehicle model. Model must be between 1-50 characters.");
        }
    }

    public static void validateIds(int... ids) throws GarageException {
        for (int id : ids) {
            if (id <= 0) {
                throw new GarageException("Invalid ID. ID must be a positive number.");
            }
        }
    }
}