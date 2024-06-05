package com.magadiflo.asociaciones.jpa.app.unidireccional.v2.ManyToMany.resources;

import com.magadiflo.asociaciones.jpa.app.unidireccional.v2.ManyToMany.dto.RegisterOrderDTO;
import com.magadiflo.asociaciones.jpa.app.unidireccional.v2.ManyToMany.entities.Order;
import com.magadiflo.asociaciones.jpa.app.unidireccional.v2.ManyToMany.entities.OrderProduct;
import com.magadiflo.asociaciones.jpa.app.unidireccional.v2.ManyToMany.entities.OrderProductPK;
import com.magadiflo.asociaciones.jpa.app.unidireccional.v2.ManyToMany.entities.Product;
import com.magadiflo.asociaciones.jpa.app.unidireccional.v2.ManyToMany.repositories.OrderProductRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "/unidireccional/v2/many-to-many/orders-products")
public class OrderProductController {

    private final OrderProductRepository orderProductRepository;

    public OrderProductController(OrderProductRepository orderProductRepository) {
        this.orderProductRepository = orderProductRepository;
    }

    @PostMapping
    public ResponseEntity<Long> saveOrderWithProducts(@RequestBody RegisterOrderDTO dto) {
        Order order = new Order();
        order.setId(dto.orderId());

        dto.orderProductIds().forEach(orderProductIdDTO -> {
            Long productId = orderProductIdDTO.productId();
            Integer quantity = orderProductIdDTO.quantity();
            Double totalPrice = orderProductIdDTO.totalPrice();

            Product product = new Product();
            product.setId(productId);

            OrderProductPK orderProductPK = new OrderProductPK();
            orderProductPK.setOrder(order);
            orderProductPK.setProduct(product);

            OrderProduct orderProduct = new OrderProduct();
            orderProduct.setId(orderProductPK);
            orderProduct.setQuantity(quantity);
            orderProduct.setTotalPrice(totalPrice);

            this.orderProductRepository.save(orderProduct);
        });

        return ResponseEntity.ok(order.getId());
    }

}
