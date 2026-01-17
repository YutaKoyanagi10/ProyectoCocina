package org.proyectococina.domain.dto;

public class SupplierDTO {
    private Long id;
    private String name;
    private String contactInfo;
    private String insertedAt;
    private String updatedAt;

    public SupplierDTO() {}

    public SupplierDTO(Long id, String name, String contactInfo, String insertedAt, String updatedAt) {
        this.id = id;
        this.name = name;
        this.contactInfo = contactInfo;
        this.insertedAt = insertedAt;
        this.updatedAt = updatedAt;
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

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getInsertedAt() {
        return insertedAt;
    }

    public void setInsertedAt(String insertedAt) {
        this.insertedAt = insertedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}