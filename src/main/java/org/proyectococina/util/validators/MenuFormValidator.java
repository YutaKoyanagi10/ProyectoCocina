package org.proyectococina.util.validators;

import javafx.scene.control.ComboBox;
import java.util.Map;

public class MenuFormValidator {

	public static boolean validateFields(String nameFieldValue, Object startDateValue) {
		if (nameFieldValue == null || nameFieldValue.isBlank() || startDateValue == null) {
			return false;
		}
		return true;
	}

	public static boolean allCombosSelected(Map<String, ComboBox<String>> menuSelectors) {
		for (ComboBox<String> combo : menuSelectors.values()) {
			String value = combo.getValue();
			if (value == null || value.isBlank()) {
				return false;
			}
		}
		return true;
	}
}
