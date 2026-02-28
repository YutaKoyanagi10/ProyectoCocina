package org.proyectococina.util.validators;

public class SupplierFormValidator {
    public static boolean validate(String name, String contact) {
        if (name == null || name.isBlank()) {
            return false;
        }
        if (contact == null || contact.isBlank()) {
            return false;
        }
        return true;
    }
    
}
