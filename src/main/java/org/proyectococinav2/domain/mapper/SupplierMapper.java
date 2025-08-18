package org.proyectococinav2.domain.mapper;

import org.proyectococinav2.domain.dto.SupplierDTO;
import org.proyectococinav2.domain.model.Supplier;

public class SupplierMapper {
    public static SupplierDTO toDTO(Supplier supplier) {
        SupplierDTO dto = new SupplierDTO();
         dto.setId(supplier.getId());
         dto.setName(supplier.getName());
         dto.setContactInfo(supplier.getContactInfo());
         return dto;
    }
    public static Supplier toModel(SupplierDTO dto) {
        Supplier supplier = new Supplier();
        supplier.setId(dto.getId());
        supplier.setName(dto.getName());
        supplier.setContactInfo(dto.getContactInfo());
        return supplier;
    }
}
