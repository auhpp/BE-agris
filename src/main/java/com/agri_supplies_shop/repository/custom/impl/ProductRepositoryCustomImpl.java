package com.agri_supplies_shop.repository.custom.impl;

import com.agri_supplies_shop.dto.request.SearchProductRequest;
import com.agri_supplies_shop.entity.Product;
import com.agri_supplies_shop.repository.custom.ProductRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {
    @PersistenceContext
    private EntityManager entityManager;

    private void joinTable(SearchProductRequest searchProductRequest, StringBuilder query) {
        if (searchProductRequest.getPriceFrom() != null || searchProductRequest.getPriceTo() != null) {
            query.append("  INNER JOIN product_variant_value pvv ON p.id = pvv.product_id ");
        }
    }

    private void handleQuery(SearchProductRequest searchProductRequest, StringBuilder where) {
        if (searchProductRequest.getName() != null) {
            where.append(" AND p.name LIKE '%" + searchProductRequest.getName() + "%' ");
        }
        if (searchProductRequest.getCategoryId() != null) {
            where.append(" AND p.category_id = " + searchProductRequest.getCategoryId());
        }
        if (searchProductRequest.getPriceFrom() != null) {
            where.append(" AND pvv.price >= " + searchProductRequest.getPriceFrom());
        }
        if (searchProductRequest.getPriceTo() != null) {
            where.append(" AND pvv.price <= " + searchProductRequest.getPriceTo());
        }
    }

    @Override
    public List findProduct(SearchProductRequest searchProductRequest, int page, int size) {
        StringBuilder query = new StringBuilder(" SELECT p.* FROM product p ");
        joinTable(searchProductRequest, query);
        StringBuilder where = new StringBuilder(" WHERE 1 = 1 ");
        handleQuery(searchProductRequest, where);
        query.append(where);
        query.append(" GROUP BY p.id");
        return this.entityManager.createNativeQuery(query.toString(), Product.class)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList()
                ;
    }
}
