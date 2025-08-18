package org.proyectococinav2.domain.model;

public class Ingredient {
    private long id;
    private String name;
    private long supplierId;

    public Ingredient() {}

    public Ingredient(long id, String name, long supplierId) {
        this.id = id;
        this.name = name;
        this.supplierId = supplierId;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public long getSupplierId() {
        return supplierId;
    }
    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }
}
