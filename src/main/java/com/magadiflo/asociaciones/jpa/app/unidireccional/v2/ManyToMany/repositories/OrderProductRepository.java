package com.magadiflo.asociaciones.jpa.app.unidireccional.v2.ManyToMany.repositories;

import com.magadiflo.asociaciones.jpa.app.unidireccional.v2.ManyToMany.entities.OrderProduct;
import com.magadiflo.asociaciones.jpa.app.unidireccional.v2.ManyToMany.entities.OrderProductPK;
import org.springframework.data.repository.CrudRepository;

public interface OrderProductRepository extends CrudRepository<OrderProduct, OrderProductPK> {

}
