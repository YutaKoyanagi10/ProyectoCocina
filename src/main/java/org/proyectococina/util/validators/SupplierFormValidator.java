package org.proyectococina.util.validators;

public class SupplierFormValidator {
    public static boolean validate(String nombre, String contacto) {
        if (nombre == null || nombre.isBlank()) {
            return false;
        }
        if (contacto == null || contacto.isBlank()) {
            return false;
        }
        return true;
    }
    
}
