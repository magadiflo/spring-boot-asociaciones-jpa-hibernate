package com.magadiflo.asociaciones.jpa.app.unidireccional.v2.ManyToMany.dto;

public record OrderProductIdDTO(Long productId,
                                Integer quantity,
                                Double totalPrice) {
}
