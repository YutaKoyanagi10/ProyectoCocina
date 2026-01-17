package org.proyectococinav2.domain.model;

import org.proyectococinav2.exception.InvalidMeasurementUnitException;

public enum MeasurementUnit {
    GRAMO("g"),
    KILOGRAMO("kg"),
    MILILITRO("ml"),
    LITRO("l"),
    CUCHARADA("cda"),
    CUCHARADITA("cdta"),
    TAZA("taza"),
    UNIDAD("u");

    private final String abreviatura;

    MeasurementUnit(String abreviatura) {
        this.abreviatura = abreviatura;
    }

    public String getAbreviatura() {
        return abreviatura;
    }

    @Override
    public String toString() {
        return this.abreviatura;
    }

    public static MeasurementUnit fromString(String text) {
        for (MeasurementUnit unit : MeasurementUnit.values()) {
            if (unit.abreviatura.equalsIgnoreCase(text)) {
                return unit;
            }
        }
        throw new InvalidMeasurementUnitException("Unidad de medida no válida: " + text);
    }
}