package com.magadiflo.asociaciones.jpa.app.unidireccional.v2.ManyToMany.repositories;

import com.magadiflo.asociaciones.jpa.app.unidireccional.v2.ManyToMany.entities.Order;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import javax.persistence.Tuple;
import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {
    @Query(value = """
            SELECT o.id AS orderId, o.order_number AS orderNumber, o.order_date AS orderDate, o.status AS status,
                    op.quantity AS quantity, op.total_price AS totalPrice,
                    p.id AS productId, p.name AS name, p.price AS price
            FROM orders AS o
                INNER JOIN orders_products AS op ON(o.id = op.order_id)
                INNER JOIN products AS p ON(op.product_id = p.id)
            """, nativeQuery = true)
    List<Tuple> findOrdersWithTheirProducts();
}
