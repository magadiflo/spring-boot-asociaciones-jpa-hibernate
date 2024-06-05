package com.magadiflo.asociaciones.jpa.app.unidireccional.v2.ManyToMany.resources;

import com.magadiflo.asociaciones.jpa.app.unidireccional.v2.ManyToMany.repositories.OrderRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.Tuple;
import java.math.BigInteger;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/unidireccional/v2/many-to-many/orders")
public class OrderController {

    private final OrderRepository orderRepository;

    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    @GetMapping
    public ResponseEntity<?> getOrdersProducts() {
        List<Tuple> ordersWithTheirProducts = this.orderRepository.findOrdersWithTheirProducts();
        List<Map<String, Object>> rows = new ArrayList<>();
        if (!ordersWithTheirProducts.isEmpty()) {
            ordersWithTheirProducts.forEach(tuple -> {
                Map<String, Object> row = new HashMap<>();

                BigInteger orderId = tuple.get("orderId", BigInteger.class);
                String orderNumber = tuple.get("orderNumber", String.class);
                Date orderDate = tuple.get("orderDate", Date.class);
                String status = tuple.get("status", String.class);
                Integer quantity = tuple.get("quantity", Integer.class);
                Double totalPrice = tuple.get("totalPrice", Double.class);
                BigInteger productId = tuple.get("productId", BigInteger.class);
                String name = tuple.get("name", String.class);
                Double price = tuple.get("price", Double.class);

                row.put("orderId", orderId);
                row.put("orderNumber", orderNumber);
                row.put("orderDate", orderDate);
                row.put("status", status);
                row.put("quantity", quantity);
                row.put("totalPrice", totalPrice);
                row.put("productId", productId);
                row.put("name", name);
                row.put("price", price);

                rows.add(row);
            });
        }
        return ResponseEntity.ok(rows);
    }


}
