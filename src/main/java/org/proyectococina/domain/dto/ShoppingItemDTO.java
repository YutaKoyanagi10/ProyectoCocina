package org.proyectococina.domain.dto;

public record ShoppingItemDTO(
    String ingredientName,
    double totalAmount,
    String unit
) {}