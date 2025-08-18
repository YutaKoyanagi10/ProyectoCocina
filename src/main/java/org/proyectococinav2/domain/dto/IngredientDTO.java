package org.proyectococinav2.domain.dto;

public class IngredientDTO {
    private Long id;
    private String name;
    private Long supplierId;
    private SupplierDTO supplier;

    public IngredientDTO() {}

    public IngredientDTO(Long id, String name, Long supplierId) {
        this.id = id;
        this.name = name;
        this.supplierId = supplierId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public SupplierDTO getSupplier() {
        return supplier;
    }

    public void setSupplier(SupplierDTO supplier) {
        this.supplier = supplier;
    }
}