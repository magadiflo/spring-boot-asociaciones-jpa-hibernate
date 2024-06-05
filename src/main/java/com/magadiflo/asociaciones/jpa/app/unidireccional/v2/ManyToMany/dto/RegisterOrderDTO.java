package com.magadiflo.asociaciones.jpa.app.unidireccional.v2.ManyToMany.dto;

import java.util.List;

public record RegisterOrderDTO(Long orderId,
                               List<OrderProductIdDTO> orderProductIds) {
}
